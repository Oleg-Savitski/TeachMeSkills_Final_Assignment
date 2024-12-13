package com.teachmeskills.application.services.analyzer;

import com.teachmeskills.application.exception.FileAnalyzerException;

import java.io.File;
/**
 * Interface for the implementation of file analysis functionalities.
 * Provides a contract for analyzing files and validating the contents of files.
 */
public interface IFileAnalyzer {

    void performFileAnalysis(File file) throws FileAnalyzerException;

    boolean checkFileContentValidity(File file) throws FileAnalyzerException;
}