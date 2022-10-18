using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace bnmmoney.module
{

    [XmlRoot(ElementName = "ValCurs")]
    public class ValCurs
    {
        [XmlElement(ElementName = "Valute")]
        public List<Valute> Valute { get; set; }

    }
}
