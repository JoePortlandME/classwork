using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace COS470
{
    class LINQtoXML
    {

        

        private static Person[] People = {
                                      new Person("John", "Brooks", "Male", "Democrat", 150000, "3803 Middle Wagon Bend,Hourglass,Maine,04203-3206"),
                                      new Person("Judy", "Bloom", "Female","Republican", 2000000, "6455 Sleepy Gate,Grandview Beach,Maine,04945-4255"),
                                      new Person("Hunter", "Thomas", "Male", "Democrat", 75000, "8961 Pleasant Mountain Harbour,Keeler,Maine,04073-7494"),
                                      new Person("Bill", "Nye", "Male", "Independent",  1000000, "672 Wishing Limits,Treat,Maine,04694-0613"),
                                      new Person("Mary", "Poppins", "Female", "Democrat", 25000, "4308 Quiet Zephyr Pike,Ducktown,Maine,04696-2603"),
                                      new Person("Jose", "Lopez", "Male", "Democrat", 66000, "1733 Emerald River Street,Euthtilloga,Maine,04273-7050"),
                                      new Person("Rachel", "Ray", "Female", "Republican", 220000, "3337 Heather Grove,Killgubbin,Maine,04587-7459"),
                                      new Person("George", "Thompson", "Male", "Republican", 160000, "7662 Misty Court,Diagonal,Maine,04236-5726"),
                                      new Person("Lois", "Lane", "Female", "Democrat",  98000, "3962 Umber Quail Wynd,Pinchtown,Maine,04787-2900"),
                                      new Person("Albert", "James", "Male", "Democrat",  72000, "5272 Thunder Estates,Noel,Maine,04808-6536"),
                                      new Person("Alicia", "Brooks", "Male", "Democrat", 900000, "6161 Grand Lagoon Woods,Marengo,Maine,04919-1269"),
                                      new Person("Charles", "Bloomingdale", "Female", "Republican", 880000, "5056 Cinder Park,Napoleon,Maine,04561-6834"),
                                      new Person("Pirate Bob", "Thomas", "Male", "Democrat", 776500, "2271 Old Goose Thicket,Cozy Nook,Maine,04701-5731"),
                                      new Person("Franic", "Deboise", "Male", "Independent",  1000000, "3337 Heather Grove,Killgubbin,Maine,04587-7459"),
                                      new Person("Walter", "Francis", "Female", "Democrat",  54800, "9043 Rustic Dell,Loveladies,Maine,04215-6908"),
                                      new Person("Chloe", "Luis", "Male", "Democrat",  699000, "4213 Clear Fox Point,Fairdealing,Maine,04901-8952"),
                                      new Person("Herbert", "Rumfield", "Female", "Republican",  2899000, "7662 Misty Court,Diagonal,Maine,04236-5726"),
                                      new Person("Ashley", "Thompson", "Male", "Republican",  9854400, "2925 Bright Island Passage,Factory,Maine,04759-4339"),
                                      new Person("Jackie", "Robinson", "Female", "Democrat",  938000, "4546 Iron Crest,Osoyoos,Maine,04820-2958"),
                                      new Person("Albert", "Einstein", "Male", "Democrat",  10000000, "3962 Umber Quail Wynd,Pinchtown,Maine,04787-2900")
                                 };
        


        public static void Main()
        {

            XElement fromFile = XElement.Load(@"\People.xml");

            int count = fromFile.Elements().Count();

            foreach (Person p in People)
            {
                XElement newPerson =
                    new XElement("person", new XAttribute("id", ++count),
                        new XElement("firstname", p.firstName),
                        new XElement("lastname", p.lastName),
                        new XElement("gender", p.gender),
                        new XElement("partyaffil", p.partyAffil),
                        new XElement("address",
                            new XElement("street", p.Street),
                            new XElement("city", p.City),
                            new XElement("state", p.State),
                            new XElement("zip", p.Zip))
                            )
                            ;
                fromFile.Add(newPerson);
            }


        }
    }
    }

