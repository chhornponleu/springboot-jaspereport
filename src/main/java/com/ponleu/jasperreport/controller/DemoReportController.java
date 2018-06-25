package com.ponleu.jasperreport.controller;


import com.ponleu.jasperreport.data.DemoDTO;
import com.ponleu.jasperreport.service.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/download")
public class DemoReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping("/xlsx")
    public HttpEntity<?> downlaodXlsx(HttpServletResponse response) throws JRException, IOException {

        Map<String, Object> params = new HashMap<>();


        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                Arrays.asList(
                        new DemoDTO(1, "Lambo", 300000L),
                        new DemoDTO(2, "Bugati", 300000L),
                        new DemoDTO(3, "Fort", 300000L),
                        new DemoDTO(4, "Audi", 300000L)
                )
        );

        // Retrieve template
        InputStream inputStream = getClass().getResourceAsStream("/reports/demo.jrxml");

        // Convert template to JasperDesign
        JasperDesign jd = JRXmlLoader.load(inputStream);

        // Compile design to JasperReport
        JasperReport jr = JasperCompileManager.compileReport(jd);

        // Create the JasperPrint object
        // Make sure to pass the JasperReport, report parameters, and data source
        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, params, dataSource);

        final byte[] data = reportService.getReportXlsx(jasperPrint);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=demo-report.xlsx");
        header.setContentLength(data.length);

        return new HttpEntity<>(data, header);
    }


    @RequestMapping("/pdf")
    public HttpEntity<byte[]> downlaodPdf(HttpServletResponse response) throws JRException {

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                Arrays.asList(
                        new DemoDTO(1, "Lambo", 300000L),
                        new DemoDTO(2, "Bugati", 300000L),
                        new DemoDTO(3, "Fort", 300000L),
                        new DemoDTO(4, "Audi", 300000L)
                )
        );

        String filename = "/Users/macintoshhd/Desktop/demo.jasper";
        File compiledFile = new File(filename);
        JasperReport jr;

        if (compiledFile.exists()) {
            jr = (JasperReport) JRLoader.loadObject(compiledFile);
        } else {
            // Retrieve template
            InputStream inputStream = getClass().getResourceAsStream("/reports/demo.jrxml");

            // Convert template to JasperDesign
            JasperDesign jd = JRXmlLoader.load(inputStream);

            // Compile design to JasperReport
            jr = JasperCompileManager.compileReport(jd);

            JRSaver.saveObject(jr, filename);
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, new HashMap<>(), dataSource);

        final byte[] data = reportService.getReportPdf(jasperPrint);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=demo-report.pdf");
        header.setContentLength(data.length);

        return new HttpEntity<>(data, header);
    }

    @RequestMapping("/html")
    public HttpEntity<?> downlaodHtml(HttpServletResponse response) throws JRException, IOException {

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                Arrays.asList(
                        new DemoDTO(1, "Lambo", 300000L),
                        new DemoDTO(2, "Bugati", 300000L),
                        new DemoDTO(3, "Fort", 300000L),
                        new DemoDTO(4, "Audi", 300000L)
                )
        );

        // Retrieve template
        InputStream inputStream = getClass().getResourceAsStream("/reports/demo.jrxml");

        // Convert template to JasperDesign
        JasperDesign jd = JRXmlLoader.load(inputStream);

        // Compile design to JasperReport
        JasperReport jr = JasperCompileManager.compileReport(jd);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, new HashMap<>(), dataSource);

        final String data = reportService.getReportHtml(jasperPrint);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(MediaType.TEXT_HTML_VALUE));
        header.setContentLength(data.length());

        return new HttpEntity<>(data, header);
    }

}
