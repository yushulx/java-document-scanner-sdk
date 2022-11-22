package com.dynamsoft.ddn;

public class Template {
    public final static String binary = "{\"GlobalParameter\":{\"Name\":\"GP\"},\"ImageParameterArray\":[{\"Name\":\"IP-1\",\"NormalizerParameterName\":\"NP-1\"}],\"NormalizerParameterArray\":[{\"Name\":\"NP-1\",\"ColourMode\": \"ICM_BINARY\" }]}";

    public final static String color = "{\"GlobalParameter\":{\"Name\":\"GP\"},\"ImageParameterArray\":[{\"Name\":\"IP-1\",\"NormalizerParameterName\":\"NP-1\"}],\"NormalizerParameterArray\":[{\"Name\":\"NP-1\",\"ColourMode\": \"ICM_COLOUR\" }]}";

    public final static String grayscale = "{\"GlobalParameter\":{\"Name\":\"GP\"},\"ImageParameterArray\":[{\"Name\":\"IP-1\",\"NormalizerParameterName\":\"NP-1\"}],\"NormalizerParameterArray\":[{\"Name\":\"NP-1\",\"ColourMode\": \"ICM_GRAYSCALE\"}]}";
}
