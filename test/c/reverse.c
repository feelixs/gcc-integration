#include <stdio.h>
#include <stdlib.h>

/**

TODO Part 1:

In this file, you will write multiple functions:

1. reverse_arr1: will take in two inputs a char** array and the number of
elements in the array. It's responsible for reversing the input array. You
should be modifying this array in place. Meaning that you shouldn't returning anything from this function. See aren't pointers convenient?

2. swap: will take in two elements from the array and swap them. Returns nothing.

Example:
Given an array ["hello", "my", "name", "is"], after calling reverse_arr1 will result in ["is", "name", "my", "hello"]

Then move on to reverse strings using the functions reverse_str1 and swap_chr. You will be doing the same thing as above, but instead of an array of strings, you will be reversing a single string.

FINALLY, implement as many of the other functions you can to pass at least 40 of the tests we include here.

*/

// use this function in your reverse_arr functions
void swap(char** a, char** b) {
    char* temp = *a; // temp should be initialized as the datatype we're trying to swap (string pointer)
    *a = *b; // set the pointer in a to the b's pointer
    *b = temp; // set the pointer in b to the pointer we initialized into temp
}

// use this function in your reverse_str functions
void swap_chr(char* a, char* b) {
    char temp = *a; // initialize temp variable as char
    *a = *b; // set the value of a to the value of b
    *b = temp; // set the value of b to temp
}

/**
Swap the beginning with the end index and vice versa, then move onto the subsequent adjacent indices, 
continuing to swap until the center is reached.
(Using the swap() function)
 */
void reverse_arr1(char** arr, int size) {
    int opposite;
    for (int i = 0; i < size / 2; i++) { // this is integer division, so when size is odd it will auto round down and ignore the middle character
        opposite = size - 1 - i;
        swap(&arr[i], &arr[opposite]); // swap the first half of the string with the second half
    }
}

/**
Iterate over the input array, and append each element to the opposite end of the 'backwards' array. Then set 'arr' = 'backwards'.
 */
void reverse_arr2(char** arr, int size) {
    char** backwards = malloc(size);
    int opposite;
    for (int i = size - 1; i >= 0; i--) { // compute arr backwards
        opposite = size - 1- i;
        backwards[opposite] = arr[i];
    }

    for (int i = 0; i < size; i++) {
        arr[i] = backwards[i]; // set the original arr to its backwards equivalent
    }
    free(backwards);
}

/**
Create pointers to the beginning and end elements of the input string array, 
then continue swapping the beginning and end, moving the ends into the middle-adjacent indices until the center is reached.
The center is reached when i reaches the size // 2.
 */
void reverse_arr3(char** arr, int size) {
    char* temp;
    char** a = arr; // a points to the first string
    char** b = arr + size - 1; // b points to the last one

    for (int i = 0; i < size / 2; i++) { // continue the swap until we reach the middle of the string array (integer division = round down)
        temp = *a; // stores a's string in temp
        *a = *b; // copy b to a, then increment a
        a++;
        *b = temp; // copy temp to b, then decriment b
        b--;
    }
}



/**
Swap the beginning with the end index and vice versa, then move onto the subsequent adjacent indices, 
continuing to swap until the center is reached.
(Manually)
 */
void reverse_arr4(char** arr, int size) {
    int i = 0;
    char* temp;
    int opposite;
    while (i < size / 2) { // continue the swap until we reach the middle
        opposite = size - 1 - i;
        temp = arr[i]; // initialize temp variable as char

        // swap the first half of the string with the second half
        arr[i] = arr[opposite]; 
        arr[opposite] = temp;
        
        i++;
    }
}

/**
Create pointers to the beginning and end elements of the input string array, 
then continue swapping the beginning and end, moving the ends into the middle-adjacent indices until the center is reached.
The center is reached when the beginning value (a) is larger than the end value (b)
 */
void reverse_arr5(char** arr, int size) {
    char* temp;
    char** a = arr; // a points to the first string
    char** b = arr + size - 1; // b points to the last one

    while (a < b) { // continue the swap while a < b (when a is bigger, we know we've passed the middle)
        temp = *a; // stores a's string in temp
        *a++ = *b; // copy b to a, then increment a
        *b-- = temp; // copy temp to b, then decriment b
    }
}

/**
Swap the beginning with the end and vice versa, then move onto the subsequent adjacent letters, 
continuing to swap until the center is reached.
(Using the swap_chr() function)
 */
void reverse_str1(char* str, int size) {
    int opposite;
    for (int i = 0; i < size / 2; i++) { // this is integer division, so when size is odd it will auto round down and ignore the middle character
        opposite = size - 1 - i;
        swap_chr(&str[i], &str[opposite]); // swap the first half of the string with the second half
    }
}


/**
Swap the beginning with the end and vice versa, then move onto the subsequent adjacent letters, 
continuing to swap until the center is reached.
(Manually)
 */
void reverse_str2(char* str, int size) {
    int opposite;
    for (int i = 0; i < size / 2; i++) { // for half of the string
        char temp = str[i]; // store the original value
        str[i] = str[size - 1 - i]; // copy the opposite value into the original value
        opposite = size - 1 - i;
        str[opposite] = temp; // replace the opposite value with the original
    }
}

/**
Create pointers to the beginning and end of the input string, 
then continue swapping the beginning and end, moving the ends into the middle until the center is reached.
 */
void reverse_str3(char* str, int size) {
    char temp;
    char* a = str; // a points to the bginning of the string
    char* b = str + size - 1; // b points to its end

    for (int i = 0; i < size / 2; i++) { // continue the swap until we reach the middle of the string (integer division = round down)
        temp = *a; // stores a's string in temp
        *a = *b; // copy b to a, then increment a
        a++;
        *b = temp; // copy temp to b, then decriment b
        b--;
    }
}

/**
Iterate over the input string, and append each element to the opposite end of the 'backwards' str. Then set 'str' = 'backwards'.
 */
void reverse_str4(char* str, int size) {
    char backwards[size];
    int opposite;
    for (int i = size - 1; i >= 0; i--) { // compute the str backwards
        opposite = size - 1 - i;
        backwards[opposite] = str[i];
    }
    
    for (int i = 0; i < size; i++) {
        str[i] = backwards[i]; // set the original str to its backwards equivalent
    }
}

/**
Set a new 'temp' variable to 'str', then copy temp backwards into 'str'
 */
void reverse_str5(char* str, int size) {
    char* temp = malloc(size); // allocate the mem for temp var
    
    for (int i = 0; i < size; i++) { // set temp's letters to str's letters
        temp[i] = str[i];
    }

    int opposite;
    for (int i = size - 1; i >= 0; i--) { // iterate backwards through temp, setting the opposite letter in str to each index
        opposite = size - 1 - i;
        str[opposite] = temp[i];
    }
    free(temp); // free allocated mem
}