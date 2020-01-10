package com.test.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TestData {

    @JsonProperty(value = "generatedUUIDS")
    public List<UUID> generatedUUIDs = new ArrayList<>();

    @JsonProperty(value = "obtainedUUIDS")
    public List<UUID> obtainedUUIDs = new ArrayList<>();
}
