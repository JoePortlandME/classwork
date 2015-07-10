using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace COS470
{
    interface Collection<T>
    {
        bool Add(T element);
        bool Contains(T element);
        bool Equals(Object o);
        bool IsEmpty();
        bool Remove(T element);
        int Occurances(T element);
    }
}
