Course Project: Financial Document Analysis System.

1 Package structure:

application
├── exception          # Exception handling
├── launcher           # Entry points and coordination
├── model              # Document models
├── security           # Security components
├── services           # Business logic
│   ├── analyzer       # Document analysis
│   ├── authentication # Authentication
│   ├── cloud          # Cloud services
│   ├── encryption     # Encryption
│   ├── logger         # Logging
│   ├── parser         # Document parsing
│   └── statistic      # Statistics
└── utils              # Utilities and constants

2 The purpose of the project:

Development of software for automated processing of financial documents using Java, including:
- Parsing of text files.
- Extracting financial information.
- Statistical analysis of documents.

3 Technology stack:

Programming language: Java 21.
Main libraries:
 -Apache Commons.
 -Google ZXing (for QR code generation).
 -AWS SDK (for cloud storage).

4 Key components of the system.

a. Document parsing:
 -Support for text files (.txt).
 -Extracting data about receipts, invoices, and orders.
 -Using regular expressions for parsing.

b. Security system:
 -Two-factor authentication.
 -Encryption of credentials.
 -Validation of login and password.
 -Session management.

c. Statistical analysis:
 -Calculation of total values according to documents.
 -Generating reports.
 -Console visualization of statistics.

5 Implementation features:

a. Safety:
 -Custom encryption mechanism.
 -Protection against incorrect data entry.
 -Restriction of authorization attempts.

b. Document processing:
 -Analyzing files filtered by year.
 -Processing invalid documents.
 -Moving incorrect files.

c. Logging:
 -Asynchronous logging.
 -Support levels: Info, Warning, Error.
 -Color coding of console output.

6 Functional features:

 -Downloading documents from the specified directory.
 -Parsing and analysis of financial documents.
 -Statistical accounting.
 -Uploading statistics to a file.
 -Optional statistics upload to cloud storage (AWS S3).
 
7 Architectural principles:
 
 -Modularity.
 -Using interfaces.
 -Handling exceptional situations.
 -Extensibility of components.

8 Limitations of the current version:
   
 -Support for text files only.
 -Parsing only certain formats of financial documents.
 -Hard-coded paths for files.
 -Limited configuration via the properties file.
 
9 Project results:

 -A mechanism for secure document processing has been implemented.
 -A statistical accounting system has been created.
 -Basic fault tolerance is provided.
 -Demonstrated Java application design skills.
 
10 Development potential:
    
 -Expanding support for document formats.
 -Improving parsing mechanisms.
 -More flexible system setup.
 -Integration with databases.

11 Conclusion:
 
The project is a practical implementation of a financial document
processing system with an emphasis on security, statistical analysis
and modularity of architecture.