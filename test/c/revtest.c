//+reverse.c

/*
Welcome to GCC Integration!
Use Ctrl+Shift+G to compile and run the current file.
For help and documentation, visit: https://feelixs.github.io/gcc-integration/

> when compiled + ran via this plugin (only on windows -> WRONG!):
Compiling using GCC [Y:/GitHub/gcc-integration/test/c/revtest.c, reverse.c]
% gcc revtest.c reverse.c -o revtest.exe
Compilation succeeded.
Saved compiled executable as Y:/GitHub/gcc-integration/test/c/revtest.exe
Running with parameters: []. View docs on automatically adding parameters: https://feelixs.github.io/gcc-integration/config.html#adding-arguments-parameters
% Y:\GitHub\gcc-integration\test\c\revtest.exe
Program finished with exit code: -1073741819

> when manually run on windows (better):
PS Y:\GitHub\gcc-integration\test\c> .\revtest.exe
We detected no arguments. Running test suite...

   Starting tests for reverse_arr1
[PASSED Test 1: one element test]
[PASSED Test 2: three element test]
[PASSED Test 3: four element test]
[PASSED Test 4: five element test]
[PASSED Test 5: two element test]

   Starting tests for reverse_arr2
[PASSED Test 6: one element test]


> when run via this plugin on macos (correct!):
Welcome to GCC Integration!
Use ⇧⌘G to compile and run the current file.
For help and documentation, visit: https://feelixs.github.io/gcc-integration/

Compiling using GCC [/Users/michaelfelix/Documents/GitHub/gcc-integration/test/c/revtest.c, reverse.c]
% gcc revtest.c reverse.c -o revtest
Compilation succeeded.
Saved compiled executable as /Users/michaelfelix/Documents/GitHub/gcc-integration/test/c/revtest
Running with parameters: []. View docs on automatically adding parameters: https://feelixs.github.io/gcc-integration/config.html#adding-arguments-parameters
% /Users/michaelfelix/Documents/GitHub/gcc-integration/test/c/revtest
	We detected no arguments. Running test suite...

	   Starting tests for reverse_arr1
	[0;32m[PASSED Test 1: one element test]
	[0m[0;32m[PASSED Test 2: three element test]
	[0m[0;32m[PASSED Test 3: four element test]
	[0m[0;32m[PASSED Test 4: five element test]
	[0m[0;32m[PASSED Test 5: two element test]
	[0m
	   Starting tests for reverse_arr2
	[0;32m[PASSED Test 6: one element test]
	[0m[0;32m[PASSED Test 7: three element test]
	[0m[0;32m[PASSED Test 8: four element test]
	[0m[0;32m[PASSED Test 9: five element test]
	[0m[0;32m[PASSED Test 10: two element test]
	[0m
	   Starting tests for reverse_arr3
	[0;32m[PASSED Test 11: one element test]
	[0m[0;32m[PASSED Test 12: three element test]
	[0m[0;32m[PASSED Test 13: four element test]
	[0m[0;32m[PASSED Test 14: five element test]
	[0m[0;32m[PASSED Test 15: two element test]
	[0m
	   Starting tests for reverse_arr4
	[0;32m[PASSED Test 16: one element test]
	[0m[0;32m[PASSED Test 17: three element test]
	[0m[0;32m[PASSED Test 18: four element test]
	[0m[0;32m[PASSED Test 19: five element test]
	[0m[0;32m[PASSED Test 20: two element test]
	[0m
	   Starting tests for reverse_arr5
	[0;32m[PASSED Test 21: one element test]
	[0m[0;32m[PASSED Test 22: three element test]
	[0m[0;32m[PASSED Test 23: four element test]
	[0m[0;32m[PASSED Test 24: five element test]
	[0m[0;32m[PASSED Test 25: two element test]
	[0m
	   Starting tests for test_char_reverse1
	[0;32m[PASSED Test 26: palindrome test]
	[0m[0;32m[PASSED Test 27: test simple string]
	[0m[0;32m[PASSED Test 28: special characters test]
	[0m[0;32m[PASSED Test 29: leading space characters test]
	[0m[0;31m[FAILED Test 30: trailing space characters test]: Expected string: '  ecaps gniliart', Actual string: '  ecapS gniliart'
	[0m[0;31m[FAILED Test 31: Unicode Characters special test!]: Expected string: '��㓁�にちは', Actual string: 'はちにんこ'
	[0m
	   Starting tests for test_char_reverse2
	[0;32m[PASSED Test 32: palindrome test]
	[0m[0;32m[PASSED Test 33: test simple string]
	[0m[0;32m[PASSED Test 34: special characters test]
	[0m[0;32m[PASSED Test 35: leading space characters test]
	[0m[0;31m[FAILED Test 36: trailing space characters test]: Expected string: '  ecaps gniliart', Actual string: '  ecapS gniliart'
	[0m[0;31m[FAILED Test 37: Unicode Characters special test!]: Expected string: '��㓁�にちは', Actual string: 'はちにんこ'
	[0m
	   Starting tests for test_char_reverse3
	[0;32m[PASSED Test 38: palindrome test]
	[0m[0;32m[PASSED Test 39: test simple string]
	[0m[0;32m[PASSED Test 40: special characters test]
	[0m[0;32m[PASSED Test 41: leading space characters test]
	[0m[0;31m[FAILED Test 42: trailing space characters test]: Expected string: '  ecaps gniliart', Actual string: '  ecapS gniliart'
	[0m[0;31m[FAILED Test 43: Unicode Characters special test!]: Expected string: '��㓁�にちは', Actual string: 'はちにんこ'
	[0m
	   Starting tests for test_char_reverse4
	[0;32m[PASSED Test 44: palindrome test]
	[0m[0;32m[PASSED Test 45: test simple string]
	[0m[0;32m[PASSED Test 46: special characters test]
	[0m[0;32m[PASSED Test 47: leading space characters test]
	[0m[0;31m[FAILED Test 48: trailing space characters test]: Expected string: '  ecaps gniliart', Actual string: '  ecapS gniliart'
	[0m[0;31m[FAILED Test 49: Unicode Characters special test!]: Expected string: '��㓁�にちは', Actual string: 'はちにんこ'
	[0m
	   Starting tests for test_char_reverse5
	[0;32m[PASSED Test 50: palindrome test]
	[0m[0;32m[PASSED Test 51: test simple string]
	[0m[0;32m[PASSED Test 52: special characters test]
	[0m[0;32m[PASSED Test 53: leading space characters test]
	[0m[0;31m[FAILED Test 54: trailing space characters test]: Expected string: '  ecaps gniliart', Actual string: '  ecapS gniliart'
	[0m[0;31m[FAILED Test 55: Unicode Characters special test!]: Expected string: '��㓁�にちは', Actual string: 'はちにんこ'
	[0m
	 Your code ran 55 tests.
	 Your reverse functions passed 45/45 tests needed to get full credit.
Program finished with exit code: 0
*/

#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "reverse.h"

static int tests_run = 0;
static int tests_correct = 0;
static int tests_incorrect = 0;

#define USAGE "We detected no arguments. Running test suite...\n"
#define ERRORMSG "ERROR: The 2nd argument must match the the number of elements\n Please try again.\n"
#define EXIT_FAILURE 1

/**
 * checks if the elements are properly reversed 
 * and returns an error message if not
 */
void assert_equal(char** expected, char** actual, const char* message, int len) {
  tests_run++;
  int element_correct = 0;
  int element_incorrect = 0;

  for (int i = 0; i < len; i++) {
    if (!strcmp(expected[i], actual[i])) {
      element_correct++;
      if (element_correct == len) tests_correct++;
    } else {
      element_incorrect++;
      if (element_correct == len) tests_incorrect++;
      printf("\033[0;31m"); // set color to red
      printf(
          "[FAILED Test %d: %s]: At index: %d, expected string: %s, actual "
          "string: %s\n",
          tests_run, message, i, expected[i], actual[i]);
      printf("\033[0m"); // reset text to default color
    }
  }

  // If no elements are wrong, the test passed! Print a message that says this :)
  if (element_incorrect == 0){
    printf("\033[0;32m"); //set color to green
    printf("[PASSED Test %d: %s]\n", tests_run, message);
    printf("\033[0m"); //reset text to default color
  }
}

/**
 * checks if two strings are equal and returns an error message if not
 */
void assert_string_equal(char* expected, char* actual, char* message) {
    tests_run++;
    
    if (!strcmp(expected, actual)) {
        // Strings are equal
        tests_correct++;
        printf("\033[0;32m"); // set color to green
        printf("[PASSED Test %d: %s]\n", tests_run, message);
        printf("\033[0m"); // reset text to default color
    } else {
        // Strings are not equal
        tests_incorrect++;
        printf("\033[0;31m"); // set color to red
        printf("[FAILED Test %d: %s]: Expected string: '%s', Actual string: '%s'\n",
               tests_run, message, expected, actual);
        printf("\033[0m"); // reset text to default color
    }
}

/**
 * runs tests for student implementation of reverse
 */
void test_array_reverse(void (*func)(char **arr, int size), int testnum) {
  printf("\n   Starting tests for reverse_arr%d\n", testnum);

  // test 1: test single element
  char* str1[] = {"hello"};
  func(str1, 1); // Use the function pointer
  assert_equal(str1, str1, "one element test", 1);

  // test 2: test three elements (odd number of elements)
  char* str2[] = {"hello", "my", "name"};
  char* reverse2[] = {"name", "my", "hello"};
  func(str2, 3); // Use the function pointer
  assert_equal(str2, reverse2, "three element test", 3);

  // test 3: test four elements (even number of elements)
  char* str3[] = {"hello", "my", "name", "is"};
  char* reverse3[] = {"is", "name", "my", "hello"};
  func(str3, 4); // Use the function pointer
  assert_equal(str3, reverse3, "four element test", 4);

  // test 4: test five elements
  char* str4[] = {"i", "love", "systems", "csc", "412"};
  char* reverse4[] = {"412", "csc", "systems", "love", "i"};
  func(str4, 5); // Use the function pointer
  assert_equal(str4, reverse4, "five element test", 5);

  // test 5: test 2 elements
  char* str5[] = {"csc", "412"};
  char* reverse5[] = {"412", "csc"};
  func(str5, 2); // Use the function pointer
  assert_equal(str5, reverse5, "two element test", 2);
}

/**
 * runs tests for student implementation of reverse
 */
void test_char_reverse(void (*func)(char*, int), int testnum) {
  printf("\n   Starting tests for test_char_reverse%d\n", testnum);

  // test 1: palindrome
  char str1[] = "madam"; // array of char (i.e, a string we can modify), which is modifiable
  char* rev1 = "madam";
  func(str1, 5); // should not change the string
  assert_string_equal(str1, rev1, "palindrome test");
    
  // test 2: test single string
  char str2[] = "hello"; // array of char (i.e, a string we can modify), which is modifiable
  char* rev2 = "olleh";
  func(str2, 5);
  assert_string_equal(str2, rev2, "test simple string");

  // test 3: special characters
  char str3[] = "hello, world!";
  char* rev3 = "!dlrow ,olleh";
  func(str3, 13);
  assert_string_equal(str3, rev3, "special characters test");

  // test 4: leading space characters
  char str4[] = "  leading space";
  char* rev4 = "ecaps gnidael  ";
  func(str4, 15);
  assert_string_equal(str4, rev4, "leading space characters test");

  // test 5: trailing space characters
  char str5[] = "trailing space  ";
  char* rev5 = "  ecapS gniliart";
  func(str5, 16);
  assert_string_equal(str5, rev5, "trailing space characters test");

  // test special: Unicode Characters
  char str6[] = "こんにちは";
  char* rev6 = "はちにんこ";
  func(str6, 6);
  assert_string_equal(str6, rev6, "Unicode Characters special test!");
}

/**
 * Print the number of tests passed
 */
void print_summary_stats() {
  printf("\n Your code ran %d tests. \n Your reverse functions passed %d/45 tests needed to get full credit.\n", tests_run, tests_correct);
}

/*
 * Runs tests in C for local development.
 *
 * Please complete the TODOs below. 
 */
int main(int argc, char** argv) {
  /*
    TODO Part 1:
      Uncomment each line below to test your 
      reverse array functions as you complete them.
  */ 
  //test_array_reverse(&reverse_arr1,1);
  //test_array_reverse(&reverse_arr2,2);
  //test_array_reverse(&reverse_arr3,3);
  //test_array_reverse(&reverse_arr4,4);
  //test_array_reverse(&reverse_arr5,5);

  /*
    TODO Part 1: 
      Uncomment each line below to test your
      reverse char functions as you complete them.
  */ 
  //test_char_reverse(&reverse_str1,1);
  //test_char_reverse(&reverse_str2,2);
  //test_char_reverse(&reverse_str3,3);
  //test_char_reverse(&reverse_str4,4);
  //test_char_reverse(&reverse_str5,5);

  //print_summary_stats();

  /*
  
  TODO Part 2: 

    if the number of args == 1, 
    then print the USAGE message to stdout,
    then run all of the test_array_reverse
    and test_char_reverse functions 
    you have completed, and run print_summary_stats()

    CLARIFICATION: copy and paste all of those 11 functions
  */
  if (argc == 1) {
      printf(USAGE);
      test_array_reverse(&reverse_arr1,1);
      test_array_reverse(&reverse_arr2,2);
      test_array_reverse(&reverse_arr3,3);
      test_array_reverse(&reverse_arr4,4);
      test_array_reverse(&reverse_arr5,5);
      test_char_reverse(&reverse_str1,1);
      test_char_reverse(&reverse_str2,2);
      test_char_reverse(&reverse_str3,3);
      test_char_reverse(&reverse_str4,4);
      test_char_reverse(&reverse_str5,5);
      print_summary_stats();
  } else {
    int NUM_ELMS, ACTUAL_NUM_ELMS;
    // num elms = number given as the first arg
    // actual num elms = actual count of args given after the number
    NUM_ELMS = atoi(argv[1]);
    ACTUAL_NUM_ELMS = argc - 2; // subtract 1 for the filename, and 1 for the NUM_ELMS
    if (NUM_ELMS != ACTUAL_NUM_ELMS) {
      fprintf(stderr, ERRORMSG);
      exit(EXIT_FAILURE);
    } else {
       // char* arr[num_elements]; // initializes an array of character pointers

      // HINT: loop through the remaining arguments and add them to the array

      // reverse the array using reverse_arr1
      // finally, print the reversed array to stdout
      char* arr[NUM_ELMS];
      for (int i = 0; i < NUM_ELMS; i++) {
        arr[i] = argv[i + 2]; // args are offset by 2 (filename, NUM_ELMS, ...)
      }
      reverse_arr1(arr, NUM_ELMS);
      for (int i = 0; i < NUM_ELMS; i++) {
        printf("%s ", arr[i]); // print each string of the revered array, in order (after being reversed)
      }
    }
  }
  
  /*
  TODO Part 2: 

    handle bad example: ./revtest 3 hello world

    If the num_elements does not match the number of elements 
    provided as command line arguments, then 
      print the ERRORMSG to stderr, and finally 
      exit EXIT_FAILURE;
    
    handle good example: ./revtest 2 hello world
    
    If num_elements matches the number of elements
    provided as command line arguments, then 
    run your reverse_arr1 with the arguments provided
    and print the reversed array to stdout.
  */

  /*
  // Uncomment the if statements for Part 2
  // check for the number of arguments
  if ("checkArgsHere_del_me") {
  
  } else {
    // HINT: lookup the atoi function in C to get the num_elements

    // HINT: handle bad example - check if the num_elements matches the 2nd arg

    // char* arr[num_elements]; // initializes an array of character pointers

    // HINT: loop through the remaining arguments and add them to the array

    // reverse the array using reverse_arr1
    // finally, print the reversed array to stdout
  }
  */

  return 0;
}