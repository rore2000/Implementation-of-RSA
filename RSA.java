import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class RSA {
   static Scanner read = new Scanner(System.in);
   private Random rnd = new Random();

   // Finds the greatest common divisor of two integers, a and b
   public long gcd(long a, long b) {
      if (b == 0)
         return a;
      return gcd(b, a % b);
   }

   // Find d , d=i such that (a*i mod m)=1
   public long find_d(long a, long m) {
      a = a % m;
      if (gcd(a, m) == 1) {
         for (long i = 1; i < m; i++)
            if ((a * i) % m == 1 && i != a)
               return i;
      }
      return 1;
   }

   // Generates a random prime number
   public int random_prime() {
      boolean found = false;
      int p = 0;
      while (!found) {
         p = rnd.nextInt(10000);
         found = isPrime(p); // check if prime?
      }
      return p;
   }

   // Check if a number is prime or not
   // True -> prime False if not
   public boolean isPrime(int p) {
      if (p < 0 || p == 0 || p == 1)
         return false;

      if (p == 2)
         return true;

      for (int i = 2; i < p; i++)
         if (p % i == 0)
            return false;
      return true;
   }

   // Generate Keys method
   // Output n_e_d an array of keys
   public long[] generate_keys() {
      long[] n_e_d = new long[3];
      long p, q, e, n, d, k, gcd;
      p = random_prime();
      q = random_prime();
      // e =random_prime();
      n = p * q;
      k = (p - 1) * (q - 1);

      // Show e values
      long[] e_list = new long[10]; // show the user at most 10 values
      System.out.println("The possible values of e:");
      // e should be coprime(gcd) and 1<e<k
      int index = 0;
      for (e = 2; e < k; e++) {
         // e is for public key exponent
         if (gcd(e, k) == 1 && index < 10) {
            System.out.print(e + " ,");
            e_list[index++] = e;
         }
      }
      System.out.print("\nEnter value of e you want ->");
      e = read.nextLong();

      long zero = 0;
      boolean flag = false;

      for (int i = 0; i < e_list.length; i++) {
         if (e == e_list[i] || e == zero)
            flag = true;
      }

      if (flag == false) {
         System.out.print("invalid input please make sure you enter the correct value e of the list, Try again ->");
         e = read.nextLong();
      }

      if (e == zero) {
         System.out.println("Program Generated random e");
         e = random_prime();
         gcd = gcd(e, k);
         while ((e >= k) && (gcd(e, k) != 1)) {
            e = random_prime();
            gcd = gcd(e, k);// make sure that e<k & e, k are relatively primes
         }
      }

      d = find_d(e, k);

      n_e_d[0] = n;
      n_e_d[1] = e;
      n_e_d[2] = d;

      return n_e_d;
   }

   // Encryption method
   public BigInteger encrypt(BigInteger plaintext, BigInteger e, BigInteger n) {

      BigInteger value;
      // Value of (num^e mod n) // //*** public key (n,e)****
      value = plaintext.modPow(e, n);
      return value;
   }

   // Decryption method
   public BigInteger decrypt(BigInteger ciphertext, BigInteger d, BigInteger n) {

      BigInteger value;
      // Value of (num^d mod n) // //*** private key (n,d)****
      value = ciphertext.modPow(d, n);
      return value;
   }

   public static void main(String[] args) {
      RSA cipher = new RSA(); // object of RSA class
      BigInteger ciphertext= BigInteger.valueOf(0);
      BigInteger n= BigInteger.valueOf(0);
      BigInteger e= BigInteger.valueOf(0);
      BigInteger d= BigInteger.valueOf(0);
      System.out.println("\n");

      System.out.print("choose (1) to Encraypt text or (2) to Decrypt text->");
      int choice =read.nextInt();
      if (choice==1){
      // Frist generate the keys -> genrate private numbers and aske user what value e he/she wants
      long[] n_e_d = cipher.generate_keys();
      // System.out.println("Modulus n is: " + n_e_d[0]);
      // System.out.println("Public key e is: " + n_e_d[1]);
      // System.out.println("Private key d is: " + n_e_d[2]);

      System.out.print("Enter plaintext ->");
      int user_plaintext = read.nextInt();
      BigInteger input = BigInteger.valueOf(user_plaintext);

       n = BigInteger.valueOf(n_e_d[0]);
       e = BigInteger.valueOf(n_e_d[1]);
       d = BigInteger.valueOf(n_e_d[2]);

      System.out.println("the input(Plaintext) is: " + input);
      // Encrypt the plaintext using exponent e and modulus n
       ciphertext = cipher.encrypt(input, e, n);
      // System.out.println("ci="+ciphertext);

   }

   if(choice ==2){
      System.out.print("Enter the ciphertext->");
      ciphertext = read.nextBigInteger();
      System.out.print("Enter value n->");
      n=read.nextBigInteger();
      System.out.print("Enter value e->");
      e= read.nextBigInteger();
      System.out.print("Enter value d->");
      d= read.nextBigInteger();

      System.out.println("the input(Ciphertext) is: " + ciphertext);

   }

      // Decrypt the ciphertext and print out the plaintext
      BigInteger decryption = cipher.decrypt(ciphertext, d, n);
      System.out.println("Decrypting is: " + decryption);

      try {
         FileWriter writer = new FileWriter("output.txt");
         writer.write("plaintext is:" + decryption + " ciphertext is:" + ciphertext);
         writer.close();
         System.out.println("\nSuccessfully wrote to the file ^_^");
      } catch (IOException ex) {
         System.out.println("An error occurred.");
         ex.printStackTrace();
      }

   }
}
