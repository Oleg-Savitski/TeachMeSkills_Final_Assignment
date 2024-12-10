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