package com.ponleu.jasperreport.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportService {

    public byte[] getReportPdf(final JasperPrint jp) throws JRException {
        return JasperExportManager.exportReportToPdf(jp);
    }

    public byte[] getReportCsv(final JasperPrint jp) throws IOException, JRException {
        JRCsvExporter csvExporter = new JRCsvExporter();
        final byte[] rawBytes;
        try (ByteArrayOutputStream csvReport = new ByteArrayOutputStream()) {
            csvExporter.setExporterInput(new SimpleExporterInput(jp));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
            csvExporter.exportReport();

            rawBytes = csvReport.toByteArray();
        }
        return rawBytes;
    }

    public byte[] getReportXlsx(final JasperPrint jp) throws JRException, IOException {
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        final byte[] rawBytes;

        try (ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()) {
            xlsxExporter.setExporterInput(new SimpleExporterInput(jp));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
            xlsxExporter.exportReport();

            rawBytes = xlsReport.toByteArray();
        }
        return rawBytes;
    }

    public String getReportHtml(final JasperPrint jp) throws IOException, JRException {
        HtmlExporter htmlExporter = new HtmlExporter();
        final String rawBytes ;

        try (ByteArrayOutputStream htmlReport = new ByteArrayOutputStream()) {
            htmlExporter.setExporterInput(new SimpleExporterInput(jp));
            htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(htmlReport));
            htmlExporter.exportReport();

            rawBytes = htmlReport.toString();
        }
        return rawBytes;

    }


    public void writeResponseAsPdf(final JasperPrint jp, HttpServletResponse response) throws JRException, IOException {
        FileCopyUtils.copy(JasperExportManager.exportReportToPdf(jp), response.getOutputStream());
    }


    public void writeResponseAsXlsx(final JasperPrint jp, HttpServletResponse response) throws IOException, JRException {
        final byte[] rawBytes;
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        try (ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()) {
            xlsxExporter.setExporterInput(new SimpleExporterInput(jp));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
            xlsxExporter.exportReport();

            FileCopyUtils.copy(xlsReport.toByteArray(), response.getOutputStream());
        }
    }
}