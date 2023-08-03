package com.todo.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.todo.app.mapper.TodoMapper;

public class BatExecuteTest {

    @Mock
    private TodoMapper todoMapperMock;

    @InjectMocks
    private BatExecute batExecute;

    @BeforeEach
    public void setup() {
        // モックを初期化
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExec() {
System.out.println("テスト完了");
    }
}
