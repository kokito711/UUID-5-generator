package com.test.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class test {

    private final UuidType5Generator uuidGenerator;

    @Autowired
    public test(UuidType5Generator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @RequestMapping(value = "/getuuids", method = GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<TestData> test(@PathParam ("path") String path){
        var generatedPathTest ="path1.path2.path3.path4.path5.path6";
        TestData testData = new TestData();
        //Generating UUIDS
        String[] generatedPathList = generatedPathTest.split("\\.");
        for (var e: generatedPathList) {
            UUID uuid;
            if (testData.generatedUUIDs.isEmpty()) {
                uuid = uuidGenerator.generateUUID(e);
            } else {
                uuid = uuidGenerator.generateUUID(testData.generatedUUIDs.get(testData.generatedUUIDs.size()-1), e);
            }
            testData.generatedUUIDs.add(uuid);
        }

        String[] providedPathList = path.split("\\.");
        //Generating UUIDS
        for (var e: providedPathList) {
            UUID uuid;
            if (testData.obtainedUUIDs.isEmpty()) {
                uuid = uuidGenerator.generateUUID(e);
            } else {
                uuid = uuidGenerator.generateUUID(testData.generatedUUIDs.get(testData.generatedUUIDs.size()-1), e);
            }
            testData.obtainedUUIDs.add(uuid);
        }
        return ResponseEntity.ok(testData);
    }
}
