using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace COS470
{
    class Bag<T> : Collection<T>, IEnumerable<T>
    {
        public List<T> list = new List<T>();

        public IEnumerator<T> GetEnumerator()
        {
            return list.GetEnumerator();
        }

        public IEnumerator<T> Iterator()
        {
            return list.GetEnumerator();
        }

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }

        public bool Add(T element)
        {
            return list.Add(element);
        }

        public bool Remove(T element)
        {
            return list.Remove(element);
        }

        public bool Contains(T element)
        {
            return list.Contains(element);
        }

        public bool IsEmpty()
        {
            return list.IsEmpty();
        }

        public Bag<T> union(Bag<T> c)
        {
            Bag<T> newBag = new Bag<T>();
            int count = 0;
            int listCount = 0;

            foreach (T element in list)
            {
                listCount = 0;
                if (!newBag.Contains(element))
                {
                    foreach (T item in list)
                        if (item.Equals(element))
                            listCount++;

                    count = Math.Max(listCount, c.Occurances(element));

                    for (int i = 0; i < count; i++)
                        newBag.Add(element);
                }
            }

            foreach (T element in c.list)
            {
                if (!newBag.Contains(element))
                    newBag.Add(element);
            }

            return newBag;
        }

        public Bag<T> intersection(Bag<T> c)
        {
            Bag<T> newBag = new Bag<T>();
            int count = 0;

            foreach (T element in c.list)
            {
                if (!newBag.Contains(element))
                {
                    int listCount = 0;
                    foreach (T item in list)
                        if (item.Equals(element))
                            listCount++;

                    count = Math.Min(listCount, c.Occurances(element));

                    for (int i = 0; i < count; i++)
                        newBag.Add(element);

                }
            }

            return newBag;
        }

        public int Occurances(T element)
        {
            int count = 0;
            foreach (T search in list)
                if (search.Equals(element))
                    count++;
            return count;
        }
    }
}
