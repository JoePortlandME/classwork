using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace COS470
{
    class OrderedBagTest
    {

        private static int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        private static String[] words = { "one", "two", "thee", "four", "five",
			"six", "seven", "eight", "nine", "ten" };

        static void Main()
        {

            OrderedBag<int> newBag = new OrderedBag<int>();
            OrderedBag<int> newBag2 = new OrderedBag<int>();
            OrderedBag<int> newBag3 = new OrderedBag<int>();

            OrderedBag<int> newOrderedBag = new OrderedBag<int>();
            OrderedBag<int> newOrderedBag2 = new OrderedBag<int>();
            OrderedBag<int> newOrderedBag3 = new OrderedBag<int>();

            for (int i = 4; i >= 0; i--)
            {
                newBag2.Add(numbers[i]);
                newOrderedBag2.Add(numbers[i]);
            }

            for (int i = 5; i < 10; i++)
            {
                newBag.Add(numbers[i]);
                newBag2.Add(numbers[i]);
                newOrderedBag.Add(numbers[i]);
                newOrderedBag2.Add(numbers[i]);
            }

            newBag3 = newBag2.Union(newBag);

            Console.WriteLine("Union of 6-10 & 1-10 :");
            Console.WriteLine("Unordered:");
            Console.WriteLine();

            foreach (int a in newBag3.list)
                Console.Write(a + "  ");

            Console.WriteLine();
            Console.WriteLine("Ordered:");
            Console.WriteLine();

            foreach (int a in newOrderedBag3.list)
                Console.Write(a + "  ");

            Console.WriteLine();
            Console.WriteLine("Adding 9-14 to ordered and unordered list.");

            newBag2.Add(12);
            newBag2.Add(10);
            newBag2.Add(11);
            newBag2.Add(14);
            newBag2.Add(13);
            newBag2.Add(9);
            newOrderedBag2.Add(12);
            newOrderedBag2.Add(10);
            newOrderedBag2.Add(11);
            newOrderedBag2.Add(14);
            newOrderedBag2.Add(13);
            newOrderedBag2.Add(9);

            Console.WriteLine();
            Console.WriteLine("Unordered Contains 23? : " + newBag3.Contains(23));
            Console.WriteLine("Ordered Contains 23? : " + newOrderedBag3.Contains(23));

            Console.WriteLine();

            newBag3 = newBag.Intersection(newBag2);
            newOrderedBag3 = newOrderedBag.Intersection(newOrderedBag2);

            Console.WriteLine("Intersection of 6-10 & 1-10 :");
            Console.WriteLine();
            Console.WriteLine("Unordsered:");
            Console.WriteLine();

            foreach (int a in newBag3.list)
                Console.Write(a + "  ");

            Console.WriteLine();
            Console.WriteLine("Ordered:");
            Console.WriteLine();

            foreach (int a in newOrderedBag3.list)
                Console.Write(a + "  ");

            Console.WriteLine();
            Console.WriteLine("Unordered Contains 6? : " + newBag3.Contains(6));
            Console.WriteLine();
            Console.WriteLine("Ordered Contains 6? : " + newOrderedBag3.Contains(6));
            Console.WriteLine();
            newBag3.Remove(6);
            newOrderedBag3.Remove(6);
            Console.WriteLine();
            Console.WriteLine("After removing 6, does unordered bag contain 6? : " + newBag3.Contains(6));
            Console.WriteLine();
            Console.WriteLine("After removing 6, does ordered bag contain 6? : " + newBag3.Contains(6));

        }


    }
}
