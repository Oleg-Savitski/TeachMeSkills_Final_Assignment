Final assignment 2024.

Create a program for processing payment documents and providing financial statements.
Access to the program must be carried out by login and password.
Implement two-factor authentication using OTP and QR code using Authenticator applications.
The program should get the path to the folder with financial documents, read information
from the documents and compile statistics.

There are three types of documents: invoices, orders, and checks.
All documents are in tt format.
Each type of document has its own structure and its own title template. Sample documents will be provided.
It is necessary to process files only for the current year.
Make technical documentation of the program: solution diagram, class diagram, sequence diagram.
Implement various checks.
Implement saving logs to a separate file.

It is advisable to separate the logs: for storing general information and for storing error information.
Upon completion of the program, all invalid files must be moved to a separate folder.
The final statistics should be uploaded to a separate file. The statistics file
must be uploaded to the Amazon S3 cloud storage.
The settings for the program to work, such as the keys for S3 and the session lifetime,
must be in the properties file.

Statistics:
- total turnover for all invoices
- total turnover for all orders
- total turnover for all checks

Acceptance criteria:
- A working program.
- Clean and understandable code.
- Compliance with naming conventions for packages, classes, methods, variables.
- Javadoc comments are required for services.
- Comments in English.
- A completed, concise and clear ReadMe file. The file must be filled in English.
- All the working code should be in the master branch. The number of other branches is unlimited.
- The repository should not contain unnecessary files and folders (for example, out, target, and others).

Verification scenario:
1. Launching the program
2. The program requests credits -> login and password input
3. The program generates a QR code -> scan the code and get a temporary password to log in to the program (OTP)
4. The program requests the path to the folder -> enter the path to the folder
5. The program is executed and the results of the program are saved in a separate folder, the report is uploaded to the cloud storage

Additional technical information on the project structure
Services:
- Authorization service.
- File reading and processing service.

Packages:
- classes for describing files
- classes for recording logs
- classes for parsing files
- classes for session description
- classes with utilitarian information
- exceptions

# Course Project: Financial Document Analysis System

## 🚀 Project Overview

Automated financial document processing system developed in Java, focusing on secure and efficient document analysis.

## 📂 Package Structure
application 
├── exception        # Exception handling 
├── launcher         # Entry points and coordination 
├── model            # Document models 
├── security         # Security components 
├── services         # Business logic 
│ ├── analyzer       # Document analysis 
│ ├── authentication # Authentication 
│ ├── cloud          # Cloud services 
│ ├── encryption     # Encryption 
│ ├── logger         # Logging 
│ ├── parser         # Document parsing 
│ └── statistic      # Statistics 
└── utils            # Utilities and constants

## 🎯 Project Purpose

Development of software for automated processing of financial documents, including:
- Parsing of text files
- Extracting financial information
- Statistical analysis of documents

## 🛠️ Technology Stack

- **Language**: Java 21
- **Libraries**:
  - Apache Commons
  - Google ZXing
  - AWS SDK

## ✨ Key Features

### 1. Document Parsing
- Support for text files (.txt)
- Data extraction from receipts, invoices
- Regular expression parsing

### 2. Security System
- Two-factor authentication
- Credential encryption
- Login validation
- Secure session management

### 3. Statistical Analysis
- Document value calculations
- Report generation
- Console statistics visualization

## 🔒 Security Highlights
- Custom encryption mechanism
- Input validation
- Authorization attempt restrictions

## 📊 Functional Capabilities
- Document directory processing
- Financial document analysis
- Statistical accounting
- File-based statistics export
- Optional cloud storage upload (AWS S3)

## 🏗️ Architectural Principles
- Modular design
- Interface-based development
- Robust error handling
- Component extensibility

## 🚧 Current Limitations
- Text file support only
- Limited document format parsing
- Hard-coded file paths
- Basic configuration options

## 🔮 Future Development
- Expand document format support
- Enhance parsing mechanisms
- Improve system configurability
- Database integration

## 💡 Project Outcomes
- Secure document processing
- Statistical tracking system
- Demonstrated Java application design skills

## 📝 License
[Choose an appropriate license]

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!

---

**Developed with ❤️ using Java**

MIT License

Copyright (c) 2024 [Oleg Savitski]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
