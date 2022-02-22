# timefall
Program that finds timefall shelters using chiral frequency for USC's CSCI 201. 
Requires an input json file (sample provided) and user input of supported chiral frequencies. 
From the shelters and frequencies provided in the input, the programs finds which are available to the wristcuff and safe for the user to teleport to. 
The user is presented with 5 options: 
  list all availble shelters within chiral frequency range, 
  search for shelter by chiral frequency, 
  search for shelter by name, 
  sort shelters by chiral frequency, 
  or jump to shelter with lowest supported chiral frequency. 
If there is an available shelter, the user will teleport to it. Otherwise, they are doomed.
The programs prunes shelters thoughout the process, and output is saved to input json file. 
