using bnmmoney.module;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace bnmmoney.repository
{
    public class FileStore : IFileStore<ValCurs>
    {
        public ValCurs ReadFromXmlFile<ValCurs>(string filePath)
        {
            TextReader reader = null;
            try
            {
                var serializer = new XmlSerializer(typeof(ValCurs));
                reader = new StreamReader(filePath);
                return (ValCurs)serializer.Deserialize(reader);
            }
            finally
            {
                if (reader != null)
                    reader.Close();
            }
        }

        public void WriteToXmlFile<ValCurs>(string filePath, ValCurs objectToWrite, bool append)
        {
            TextWriter writer = null;
            try
            {
                var serializer = new XmlSerializer(typeof(ValCurs));
                writer = new StreamWriter(filePath, append);
                serializer.Serialize(writer, objectToWrite);
            }
            finally
            {
                if (writer != null)
                    writer.Close();
            }
        }
    }
}
