package com.fpt.submission.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fpt.submission.utils.JsonUtils;
import com.fpt.submission.utils.PathUtils;
import com.fpt.submission.dto.request.PracticalInfo;
import com.fpt.submission.dto.request.UploadFileDto;
import com.fpt.submission.service.SubmissionService;
import com.fpt.submission.utils.SubmissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.databind.MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionUtils submissionUtils;
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionUtils submissionUtils, SubmissionService submissionService) {
        this.submissionUtils = submissionUtils;
        this.submissionService = submissionService;
    }

    @GetMapping("/test")
    public void test() throws IOException {
        File xml = new File("test2.xml");
        ObjectWriter w = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Object o = new XmlMapper()
                .registerModule(new SimpleModule().addDeserializer(Object.class, new JsonUtils()))
                .readValue(xml, Object.class);

        System.out.println(w.writeValueAsString(o));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(w.writeValueAsString(o));
        JsonNode nodeSuiteName = node.findPath("SUITE_NAME");
        List<JsonNode> nodeSuiteSuccess = node.findValues("CUNIT_RUN_TEST_SUCCESS");
        List<JsonNode> nodeSuiteFailed = node.findValues("CUNIT_RUN_TEST_FAILURE");
    }

    @PostMapping("/submission")
    public String uploadFile(@ModelAttribute UploadFileDto file) throws IOException {
        return submissionService.submit(file);
    }

}
