package com.obs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task Application test class")
public class TaskApplicationTest {

    @Test
    public void testCreateFileSuccess() throws IOException {
        File result = TaskApplication.createFile();
        assertTrue(result.isFile());
    }

    @Test
    public void testCreateFileFailed() throws IOException{
        File result = TaskApplication.createFile();
        assertFalse(result.isFile());
    }

    @Test
    public void testGetListFilesSuccess(){
        Integer listFiles = TaskApplication.getListFiles().size();
        assertNotNull(listFiles);
    }
}
