/*
 * Sonar Groovy Plugin
 * Copyright (C) 2010-2021 SonarQube Community
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.ecocode.groovy.checks.release;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.plugins.groovy.codenarc.ActiveRulesBuilderWrapper;
import org.sonar.plugins.groovy.codenarc.CodeNarcSensor;
import org.sonar.plugins.groovy.foundation.Groovy;
import org.sonar.plugins.groovy.foundation.GroovyFileSystem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class FatAppRuleTest {
  @Rule public TemporaryFolder temp = new TemporaryFolder();

  private SensorContextTester sensorContextTester;
  private MapSettings settings = new MapSettings();

  @Before
  public void setUp() throws Exception {
    sensorContextTester = SensorContextTester.create(temp.newFolder());
    sensorContextTester.fileSystem().setWorkDir(temp.newFolder().toPath());

    sensorContextTester.setSettings(settings);
  }

  @Test
  public void should_run_code_narc_FatApp() throws IOException {

//    addFileWithContent(
//            "src/sample.groovy",
//            "android {\n" +
//            "                compileSdk 32\n" +
//            "            \n" +
//            "                defaultConfig {\n" +
//            "                    applicationId \"com.example.sampleForSonar\"\n" +
//            "                    minSdk 28\n" +
//            "                    targetSdk 32\n" +
//            "                    versionCode 1\n" +
//            "                    versionName \"1.0\"\n" +
//            "                    multiDexEnabled true\n" +
//            "            \n" +
//            "                    testInstrumentationRunner \"androidx.test.runner.AndroidJUnitRunner\"\n" +
//            "                }\n" +
//            "            \n" +
//            "                buildTypes {\n" +
//            "                    release {\n" +
//            "                        minifyEnabled false\n" +
//            "                        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'\n" +
//            "                    }\n" +
//            "                }\n" +
//            "                compileOptions {\n" +
//            "                    sourceCompatibility JavaVersion.VERSION_1_8\n" +
//            "                    targetCompatibility JavaVersion.VERSION_1_8\n" +
//            "                }\n" +
//            "                buildFeatures {\n" +
//            "                    viewBinding true\n" +
//            "                }\n" +
//            "            }"
//    );
    addFileWithContent("src/sample.groovy","package source\nclass SourceFile1 {\n}");

    ActiveRulesBuilderWrapper activeRulesBuilder =
            new ActiveRulesBuilderWrapper()
                    .addRule("org.codenarc.rule.basic.FatAppRule")
                    .setInternalKey("FatApp");
    sensorContextTester.setActiveRules(activeRulesBuilder.build());

    CodeNarcSensor sensor =
            new CodeNarcSensor(
                    sensorContextTester.activeRules(),
                    new GroovyFileSystem(sensorContextTester.fileSystem()));
    sensor.execute(sensorContextTester);

    assertThat(sensorContextTester.allIssues()).hasSize(0);
  }

  @Test
  public void should_run_code_narc() throws IOException {

    addFileWithContent("src/sample.groovy", "package source\nclass SourceFile1 {\n}");

    ActiveRulesBuilderWrapper activeRulesBuilder =
            new ActiveRulesBuilderWrapper()
                    .addRule("org.codenarc.rule.basic.EmptyClassRule")
                    .setInternalKey("EmptyClass");
    sensorContextTester.setActiveRules(activeRulesBuilder.build());

    CodeNarcSensor sensor =
            new CodeNarcSensor(
                    sensorContextTester.activeRules(),
                    new GroovyFileSystem(sensorContextTester.fileSystem()));
    sensor.execute(sensorContextTester);

    assertThat(sensorContextTester.allIssues()).hasSize(1);
  }

  private void addFileWithContent(String path, String content) {
    InputFile inputFile =
        TestInputFileBuilder.create(sensorContextTester.module().key(), path)
            .setLanguage(Groovy.KEY)
            .setType(Type.MAIN)
            .setContents(content)
            .build();
    sensorContextTester.fileSystem().add(inputFile);
  }
}
