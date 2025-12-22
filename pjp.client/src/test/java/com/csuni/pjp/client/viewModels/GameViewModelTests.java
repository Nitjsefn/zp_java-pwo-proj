package com.csuni.pjp.client.viewModels;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.testfx.framework.junit.ApplicationTest;

import com.csuni.pjp.client.JavaFXThreadingRule;
import com.csuni.pjp.client.support.ViewManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


@SpringBootTest
class GameViewModelTests {
    @Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Autowired
    private GameViewModel gameVM;

    private void _relativate_list_shall_pass_test_factory() {
        List<String> list = Arrays.asList(new String[]{"111", "22", "3", "44", "5", ""});
        String currentElement = "";
        List<String> expected = Arrays.asList(new String[]{"", "111", "22", "3", "44", "5"});
        gameVM.relativateList(list, currentElement);
        Assert.isTrue(list.equals(expected), "Lists are the same");
        fail();
    }

    @Test
    public void relativate_list_shall_pass_test_factory() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        _relativate_list_shall_pass_test_factory();
                    }
                });
            }
        });
        thread.start();
        try {
            Thread.sleep(10000);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        _relativate_list_shall_pass_test_factory();
    }
}
