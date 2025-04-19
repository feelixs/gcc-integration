# Windows Exit Codes and Error Codes

## Common Windows Exit/Error Codes for Crashes

### Negative Exit Codes
- `-1073741819` (0xC0000005): Access Violation (EXCEPTION_ACCESS_VIOLATION) - Equivalent to segmentation fault in Unix systems
- `-1073741795` (0xC000001D): Illegal Instruction (EXCEPTION_ILLEGAL_INSTRUCTION)
- `-1073741786` (0xC0000026): Invalid Parameter (STATUS_INVALID_PARAMETER)
- `-1073741571` (0xC00000FD): Stack Overflow (EXCEPTION_STACK_OVERFLOW)
- `-1073741515` (0xC0000135): DLL Not Found (STATUS_DLL_NOT_FOUND)
- `-1073741676` (0xC0000094): Integer Division by Zero (EXCEPTION_INT_DIVIDE_BY_ZERO)
- `-1073741787` (0xC0000025): Non-continuable Exception (EXCEPTION_NONCONTINUABLE_EXCEPTION)

### Hexadecimal Format (How these appear in Windows Debug)
- `0xC0000005`: Access Violation (most common crash, equivalent to SIGSEGV)
- `0xC000001D`: Illegal Instruction
- `0xC0000026`: Invalid Parameter 
- `0xC00000FD`: Stack Overflow
- `0xC0000135`: DLL Not Found
- `0xC0000094`: Integer Division by Zero
- `0xC0000025`: Non-continuable Exception

### Understanding the Format
- Windows internal error codes are often in hexadecimal format
- The negative exit codes are calculated by interpreting the hex as a signed 32-bit integer
- To convert between formats: -1073741819 = 0xC0000005 (Access Violation)

### Additional Common Exit Codes
- `1`: Generic error
- `2`: File not found
- `3`: Path not found
- `5`: Access denied
- `8`: Not enough memory
- `10`: Invalid environment
- `11`: Invalid format
- `128`: Invalid function argument/parameter
- `255`: Program terminated abnormally

## About Exit Code -1073741819 (0xC0000005)
- This is the Windows Access Violation exception
- Equivalent to a segmentation fault in Unix/Linux (SIGSEGV)
- Common causes:
  - Dereferencing NULL pointers
  - Accessing memory that has been freed
  - Buffer overflows
  - Stack corruption
  - Accessing memory outside of allocated bounds
  - Using uninitialized pointers
  - Writing to read-only memory

## Debugging Crashes with These Codes
- Use Windows Event Viewer to find crash details
- Enable crash dumps (through registry or System Properties)
- Use WinDbg or Visual Studio debugging tools to analyze crash dumps
- Look for the exception code in stack traces