using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace COS470
{
    class Person : IComparable<Person>, IComparable
    {
        public String firstName { get; set; }
        public String lastName { get; set; }
        public String gender { get; set; }
        public String partyAffil { get; set; }
        public Double netWorth { get; set; }
        public String Street { get; set; }
        public String City { get; private set; }
        public String State { get; private set; }
        public String Zip { get; private set; }


        public Person(String first, String last, String gen, String party, Double net)
        {
            firstName = first;
            lastName = last;
            gender = gen;
            partyAffil = party;
            netWorth = net;
        }

        public Person(String first, String last, String gen, String party, Double net, String Address)
        {
            firstName = first;
            lastName = last;
            gender = gen;
            partyAffil = party;
            netWorth = net;

            String[] split = Address.Split(',');
            Street = split[0];
            City = split[1];
            State = split[2];
            Zip = split[3];
        }

        public int CompareTo(Person otherPerson)
        {
            if (this.lastName.CompareTo(otherPerson.lastName) != 0)
                return this.firstName.CompareTo(otherPerson.firstName);

            return this.lastName.CompareTo(otherPerson.lastName);
        }

        public int CompareTo(object obj)
        {
            if (obj == null) return 1;

            Person otherPerson = obj as Person;
            if (otherPerson == null)
                throw new ArgumentException("Object is not a Person");

            return this.CompareTo(otherPerson);
        }


        int IComparable.CompareTo(object obj)
        {
            return CompareTo(obj);
        }

        public String ToString()
        {
            return (firstName + " " + lastName + " " + gender + " " + partyAffil + " " + netWorth + " " + Street + " " + City + " " + State + " " + Zip);
        }
    }
}
