public class GetMax {
   public static int max(int[] m) {
      int local_max = m[0];
      
      for (int i=1; i < m.length; i++) {
         if (m[i] > local_max) local_max = m[i];
      }
      
     
      return local_max;
   }
   
   private static int max(int x, int y) {
      if (x > y) return x;
      return y;
   }
   
   
   public static void main(String[] args) {
      int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};      


       System.out.println(max(numbers));
   }
}
