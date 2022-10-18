using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace bnmmoney.module
{
    [XmlRoot(ElementName = "Valute")]
    public class Valute
    {
        [XmlElement("Name")]
        public string Name { get; set; }

        [XmlElement("NumCode")]
        public int NumCode { get; set; }

        [XmlElement("CharCode")]
        public string CharCode { get; set; }

        [XmlElement("Nominal")]
        public int Nominal { get; set; }

        [XmlElement("Value")]
        public decimal Value { get; set; }

        public override string ToString()
        {
            return $"{Name}";
        }
    }
}
