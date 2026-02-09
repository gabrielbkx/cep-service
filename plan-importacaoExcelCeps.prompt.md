# Plan: Implementar Importação em Massa de CEPs via Excel

## Objetivo
Adicionar funcionalidade que permite administradores importarem arquivos Excel (.xlsx/.xls) contendo dados de CEPs que serão salvos em lote no banco de dados. O sistema processará, validará e retornará estatísticas da importação (total processado, inseridos, inválidos).

## Contexto Atual
- Já existe estrutura de pacotes `csv` com `ResponseArquivo` record para retorno de importações
- Sistema usa autenticação JWT com roles (ADMIN tem acesso a operações privilegiadas)
- `Cep` entity possui construtor `Cep(String numeroCep, String logradouro, String cidade)`
- Validação de CEP usa pattern `\\d{8}` (exatamente 8 dígitos)
- `CepRepository` possui `existsByNumeroCep()` e `saveAll()` para batch operations
- `ExcelCepDto` record já foi criado no pacote `excel.dto`

## Steps de Implementação

### 1. Adicionar Dependências Apache POI
**Arquivo:** `pom.xml`

Adicionar após a dependência do OpenCSV (se existir) ou na seção de dependencies:

```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.5</version>
</dependency>

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```

Executar: `./mvnw clean install` ou recarregar dependências Maven no IDE

### 2. Criar Exceções Customizadas
**Pacote:** `com.cep_service.cep_service.excel.exception`

**Arquivo 2.1:** `ExcelFormatoInvalidoException.java`
```java
package com.cep_service.cep_service.excel.exception;

public class ExcelFormatoInvalidoException extends RuntimeException {
    public ExcelFormatoInvalidoException(String message) {
        super(message);
    }
}
```

**Arquivo 2.2:** `ExcelProcessamentoException.java`
```java
package com.cep_service.cep_service.excel.exception;

public class ExcelProcessamentoException extends RuntimeException {
    public ExcelProcessamentoException(String message) {
        super(message);
    }

    public ExcelProcessamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 3. Criar Service de Processamento Excel
**Pacote:** `com.cep_service.cep_service.excel.service`

**Arquivo:** `ExcelProcessadorService.java`

**Responsabilidades:**
- Validar arquivo (formato .xlsx/.xls, tamanho máximo 10MB, não vazio)
- Criar Workbook apropriado (XSSFWorkbook para .xlsx, HSSFWorkbook para .xls)
- Ler primeira aba do Excel
- Validar existência de cabeçalho (linha 1)
- Processar linhas de dados (linha 2+)
- Normalizar CEPs (remover traços, pontos, espaços)
- Ignorar linhas vazias
- Limitar processamento a MAX_LINHAS (10.000)
- Retornar `List<ExcelCepDto>`

**Métodos principais:**
```java
@Service
public class ExcelProcessadorService {
    
    private static final int MAX_LINHAS = 10000;
    
    public List<ExcelCepDto> processarArquivoExcel(MultipartFile arquivo) {
        // Validar arquivo
        // Criar workbook
        // Extrair dados
        // Retornar lista
    }
    
    private void validarArquivo(MultipartFile arquivo) {
        // Verificar não nulo/vazio
        // Verificar extensão .xlsx ou .xls
        // Verificar tamanho <= 10MB
    }
    
    private Workbook criarWorkbook(MultipartFile arquivo, InputStream input) {
        // Se .xlsx -> new XSSFWorkbook(input)
        // Se .xls -> new HSSFWorkbook(input)
    }
    
    private List<ExcelCepDto> extrairDadosDoExcel(Workbook workbook) {
        // Pegar primeira aba: workbook.getSheetAt(0)
        // Validar cabeçalho (linha 0)
        // Iterar linhas (1+)
        // Converter Cell para String
        // Normalizar CEP: replaceAll("[^0-9]", "")
        // Criar ExcelCepDto
    }
    
    private String getCellValueAsString(Cell cell) {
        // Switch por CellType (STRING, NUMERIC, BOOLEAN, FORMULA, BLANK)
        // Converter para String apropriadamente
    }
    
    private boolean isRowEmpty(Row row) {
        // Verificar se todas células estão vazias/null
    }
}
```

**Imports necessários:**
```java
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import com.cep_service.cep_service.excel.dto.ExcelCepDto;
import com.cep_service.cep_service.excel.exception.*;
```

### 4. Adicionar Método de Importação no CepService
**Arquivo:** `CepService.java`

**Adicionar campo e construtor:**
```java
private final CepRepository cepRepository;
private final ExcelProcessadorService excelProcessadorService;

public CepService(CepRepository cepRepository, ExcelProcessadorService excelProcessadorService) {
    this.cepRepository = cepRepository;
    this.excelProcessadorService = excelProcessadorService;
}
```

**Adicionar método:**
```java
public ResponseArquivo importarExcel(MultipartFile arquivo) {
    // 1. Processar arquivo Excel
    List<ExcelCepDto> cepsDoExcel = excelProcessadorService.processarArquivoExcel(arquivo);
    
    // 2. Inicializar contadores
    int totalRegistros = cepsDoExcel.size();
    int totalInseridos = 0;
    int totalInvalidos = 0;
    
    // 3. Buscar CEPs existentes (otimização)
    Set<String> cepsExistentes = cepRepository.findAll()
            .stream()
            .map(Cep::getNumeroCep)
            .collect(Collectors.toSet());
    
    // 4. Processar cada CEP do Excel
    List<Cep> cepsParaSalvar = new ArrayList<>();
    
    for (ExcelCepDto dto : cepsDoExcel) {
        // Validar dados completos
        if (dto.numeroCep() == null || dto.numeroCep().trim().isEmpty() ||
            dto.logradouro() == null || dto.logradouro().trim().isEmpty() ||
            dto.cidade() == null || dto.cidade().trim().isEmpty()) {
            totalInvalidos++;
            continue;
        }
        
        // Validar formato CEP (8 dígitos)
        if (!dto.numeroCep().matches("\\d{8}")) {
            totalInvalidos++;
            continue;
        }
        
        // Verificar duplicados (banco + arquivo)
        if (cepsExistentes.contains(dto.numeroCep())) {
            totalInvalidos++;
            continue;
        }
        
        // Adicionar para salvar
        Cep novoCep = new Cep(dto.numeroCep(), dto.logradouro(), dto.cidade());
        cepsParaSalvar.add(novoCep);
        cepsExistentes.add(dto.numeroCep()); // Evitar duplicados no mesmo arquivo
        totalInseridos++;
    }
    
    // 5. Salvar em batch
    if (!cepsParaSalvar.isEmpty()) {
        cepRepository.saveAll(cepsParaSalvar);
    }
    
    // 6. Retornar estatísticas
    return new ResponseArquivo(
        String.valueOf(totalRegistros),
        String.valueOf(totalInseridos),
        String.valueOf(totalInvalidos)
    );
}
```

**Imports adicionais:**
```java
import com.cep_service.cep_service.csv.dto.ResponseArquivo;
import com.cep_service.cep_service.excel.dto.ExcelCepDto;
import com.cep_service.cep_service.excel.service.ExcelProcessadorService;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
```

### 5. Criar Endpoint no CepController
**Arquivo:** `CepController.java`

**Adicionar no final, após o método deletarCep:**
```java
@PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
@PostMapping("/importar-excel")
@Transactional
public ResponseEntity<ResponseArquivo> importarExcel(@RequestParam("arquivo") MultipartFile arquivo) {
    var resultado = cepService.importarExcel(arquivo);
    return ResponseEntity.ok(resultado);
}
```

**Imports adicionais:**
```java
import com.cep_service.cep_service.csv.dto.ResponseArquivo;
import org.springframework.web.multipart.MultipartFile;
```

### 6. Adicionar Tratamento Global de Exceções
**Arquivo:** `infra/exceptions/TratarErrosGlobais.java`

**Adicionar métodos:**
```java
@ExceptionHandler(ExcelFormatoInvalidoException.class)
public ResponseEntity<DadosErro> tratarErroExcelFormato(ExcelFormatoInvalidoException e) {
    DadosErro dadosErro = new DadosErro(e.getMessage(), 400);
    return ResponseEntity.status(400).body(dadosErro);
}

@ExceptionHandler(ExcelProcessamentoException.class)
public ResponseEntity<DadosErro> tratarErroExcelProcessamento(ExcelProcessamentoException e) {
    DadosErro dadosErro = new DadosErro(e.getMessage(), 400);
    return ResponseEntity.status(400).body(dadosErro);
}

@ExceptionHandler(ArquivoVazioException.class)
public ResponseEntity<DadosErro> tratarErroArquivoVazio(ArquivoVazioException e) {
    DadosErro dadosErro = new DadosErro(e.getMessage(), 400);
    return ResponseEntity.status(400).body(dadosErro);
}
```

**Imports adicionais:**
```java
import com.cep_service.cep_service.excel.exception.ExcelFormatoInvalidoException;
import com.cep_service.cep_service.excel.exception.ExcelProcessamentoException;
import com.cep_service.cep_service.csv.dto.exception.ArquivoVazioException;
```

## Estrutura do Arquivo Excel Esperado

### Formato
- **Extensão:** .xlsx ou .xls
- **Primeira linha:** Cabeçalho (obrigatório)
- **Linhas seguintes:** Dados dos CEPs

### Colunas (ordem fixa: 0, 1, 2)
| Coluna A (0) | Coluna B (1) | Coluna C (2) |
|--------------|--------------|--------------|
| numeroCep    | logradouro   | cidade       |
| 01310100     | Avenida Paulista | São Paulo |
| 20040020     | Praça Floriano | Rio de Janeiro |
| 30130100     | Avenida Afonso Pena | Belo Horizonte |

### Regras de Validação
- CEP deve ter exatamente 8 dígitos após normalização
- CEPs com formatação (01310-100) são aceitos e normalizados
- Todos os campos são obrigatórios
- CEPs duplicados no banco são ignorados (contam como inválidos)
- CEPs duplicados no mesmo arquivo são processados apenas uma vez
- Linhas vazias são automaticamente ignoradas
- Limite máximo: 10.000 linhas de dados
- Tamanho máximo do arquivo: 10MB

## Testes Manuais

### 1. Teste com Postman
```
POST http://localhost:8080/cep/importar-excel
Authorization: Bearer <token-admin>
Body: form-data
  - Key: arquivo (type: File)
  - Value: [selecionar arquivo .xlsx]
```

### 2. Teste com cURL
```bash
curl -X POST "http://localhost:8080/cep/importar-excel" \
  -H "Authorization: Bearer <token-admin>" \
  -F "arquivo=@ceps.xlsx"
```

### 3. Casos de Teste
- ✅ Arquivo válido com 10 CEPs únicos
- ✅ Arquivo com CEPs já existentes (verificar contagem de inválidos)
- ✅ Arquivo com CEPs formatados (01310-100)
- ✅ Arquivo com linhas vazias
- ✅ Arquivo com dados incompletos (campos vazios)
- ✅ Arquivo com CEPs inválidos (menos/mais de 8 dígitos)
- ❌ Arquivo .txt ou .pdf (deve rejeitar)
- ❌ Arquivo muito grande (> 10MB)
- ❌ Usuário não-admin (deve retornar 403)
- ❌ Token inválido (deve retornar 401)

## Resposta Esperada

### Sucesso (200 OK)
```json
{
  "totalRegistros": "100",
  "TotalInseridos": "95",
  "TitalInvalidos": "5"
}
```

### Erros Possíveis
- **400 Bad Request:** Formato inválido, arquivo vazio, erro de processamento
- **401 Unauthorized:** Token inválido ou expirado
- **403 Forbidden:** Usuário não possui role ADMIN
- **500 Internal Server Error:** Erro inesperado no servidor

## Considerações de Implementação

### Performance
- Usar `Set<String>` para verificação O(1) de CEPs existentes
- Batch insert com `saveAll()` reduz queries ao banco
- Limitar a 10.000 registros para evitar OutOfMemoryError

### Segurança
- Apenas ADMIN pode importar (`@PreAuthorize`)
- Validar tamanho do arquivo (10MB)
- Validar extensão do arquivo
- Não expor stack traces completos

### Manutenibilidade
- Separar responsabilidades (Service processa Excel, CepService gerencia lógica de negócio)
- Usar exceções customizadas para erros específicos
- Documentar regras de validação no código

### Transação
- Usar `@Transactional` no controller
- Se houver erro crítico, todas inserções são revertidas
- Importação é "tudo ou nada" (atomicidade)

## Melhorias Futuras (Opcional)

1. **Validação flexível de cabeçalhos:** Aceitar colunas em qualquer ordem lendo nomes do cabeçalho
2. **Relatório detalhado:** Retornar lista de erros por linha (ex: "Linha 5: CEP inválido")
3. **Modo de atualização:** Opção para atualizar CEPs existentes ao invés de ignorar
4. **Processamento assíncrono:** Para arquivos muito grandes, usar `@Async` e retornar ID de job
5. **Validação de endereços:** Integrar com API externa (ViaCEP) para validar se CEP é real
6. **Suporte CSV:** Adicionar endpoint similar para arquivos CSV
7. **Template download:** Endpoint para baixar arquivo Excel modelo
8. **Logs detalhados:** Registrar importações no banco (usuário, data, resultado)

## Checklist de Implementação

- [ ] Adicionar dependências Apache POI no pom.xml
- [ ] Criar ExcelFormatoInvalidoException
- [ ] Criar ExcelProcessamentoException
- [ ] Implementar ExcelProcessadorService com todos os métodos
- [ ] Atualizar CepService (adicionar campo, construtor e método importarExcel)
- [ ] Adicionar endpoint no CepController
- [ ] Adicionar handlers de exceção em TratarErrosGlobais
- [ ] Testar com arquivo Excel válido
- [ ] Testar validações (formato, tamanho, dados)
- [ ] Testar autorizações (admin vs não-admin)
- [ ] Documentar endpoint (Swagger/README)

