package com.cep_service.cep_service.excel;


import com.cep_service.cep_service.domain.cep.CepRepository;
import com.cep_service.cep_service.domain.cep.dto.DadosDetalharCep;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelProcessadorService {

    private final CepRepository cepRepository;

    public ExcelProcessadorService(CepRepository cepRepository) {
        this.cepRepository = cepRepository;
    }

    private final String COLUNA_ZERO = "ID";
    private final String COLUNA_UM = "CEP";
    private final String COLUNA_DOIS = "LOGRADOURO";
    private final String COLUNA_TRES = "CIDADE";


    public byte [] exportarCepsViaExcell() throws IOException {

        List<DadosDetalharCep> ceps = cepRepository.findAll().stream().map(DadosDetalharCep::new).toList();
        return criarPlanilhaExcel(ceps);
    }

    private byte[] criarPlanilhaExcel(List<DadosDetalharCep> ceps) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet aba = workbook.createSheet("ceps");

        // Criar a linha de cabeçalho
        Row linhaCabecalho = aba.createRow(0);
        Cell colunaId = linhaCabecalho.createCell(0);

        // Definir o valor da célula para a coluna "ID"
        colunaId.setCellValue(COLUNA_ZERO);

        // Criar a célula para a coluna "CEP"
        Cell colulaCep = linhaCabecalho.createCell(1);
        colulaCep.setCellValue(COLUNA_UM);

        Cell colunaLogradouro = linhaCabecalho.createCell(2);
        colunaLogradouro.setCellValue(COLUNA_DOIS);

        Cell colunaCidade = linhaCabecalho.createCell(3);
        colunaCidade.setCellValue(COLUNA_TRES);

        int numeroLinha = 1;
        for (DadosDetalharCep cep : ceps){

            Row linha = aba.createRow(numeroLinha);
             colunaId = linha.createCell(0);
             colunaId.setCellValue(cep.id());
             colulaCep = linha.createCell(1);
             colulaCep.setCellValue(cep.numeroCep());
             colunaLogradouro = linha.createCell(2);
             colunaLogradouro.setCellValue(cep.logradouro());
             colunaCidade = linha.createCell(3);
             colunaCidade.setCellValue(cep.cidade());

            numeroLinha++;

        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] excelBytes = outputStream.toByteArray();

        return excelBytes;

    }


}

