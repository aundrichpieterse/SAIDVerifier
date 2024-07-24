Author: Aundrich Pieterse

SAIDVerifier Overview

SAIDVerifier is a Java application designed to validate South African ID numbers. It checks the format, checksum, and validity of the ID number, extracting personal details such as birth date, gender, and citizenship status. The application also saves verification results to a log file.

Features

ID Number Validation:
Verifies that the ID number is exactly 13 digits long and contains only numeric digits. Uses the Luhn algorithm to validate the checksum. Checks if the extracted date of birth is a valid date.

Personal Information Extraction:
Displays the date of birth, age, gender, and citizenship status based on the ID number.

Result Logging:
Saves verification results, including the ID number, verification date, and result format, to a text file.

Retry and Menu Navigation:
Provides options to retry entering the ID number or navigate back to the main menu.

Help and About Sections:
Includes instructions on using the application and information about the application version and author.
