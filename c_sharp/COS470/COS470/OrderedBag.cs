using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace COS470
{
    class OrderedBag<T> : Bag<T>, OrderedCollection<T> where T : IComparable<T>
    {
        public bool Add(T element)
        {

            if (list.IsEmpty())
            {
                return list.Add(element);
            }

            IEnumerator<T> iter = list.GetEnumerator();
            iter.MoveNext();
            T search;
            int i = 0;

            do
            {
                search = iter.Current;
                if (search.CompareTo(element) >= 0)
                {
                    list.Add(i, element);
                    return true;
                }
                i++;
            } while (iter.MoveNext());

            return list.Add(element);
        }

        public bool IsOrdered()
        {

            IEnumerator<T> iter = list.GetEnumerator();

            bool ordered = true;
            iter.MoveNext();
            T element = iter.Current;
            iter.MoveNext();
            T nextElement = iter.Current;

            while (iter.MoveNext() && ordered == true)
            {
                if (element.CompareTo(nextElement) > 0)
                    ordered = false;
                element = nextElement;
                nextElement = iter.Current;
            }

            if (ordered == false)
                return false;

            return true;
        }

        public OrderedBag<T> Union(Bag<T> c)
        {
            OrderedBag<T> newBag = new OrderedBag<T>();
            int count = 0;

            int listCount;
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
                {
                    listCount = 0;

                    foreach (T item in list)
                        if (item.Equals(element))
                            listCount++;

                    count = Math.Max(listCount, c.Occurances(element));

                    for (int i = 0; i < count; i++)
                        newBag.Add(element);
                }
            }
            return newBag;
        }

        public OrderedBag<T> Intersection(Bag<T> c)
        {
            OrderedBag<T> newBag = new OrderedBag<T>();
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
    }
}
