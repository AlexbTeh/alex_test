using bnmmoney.module;
using Microsoft.Extensions.Configuration;

using System.Net.Http.Headers;
using System.Reflection;
using System.Xml.Serialization;
using bnmmoney.utilities;

namespace bnmmoney.repository
{
    public class BankRepository : IBankRepository , IFileRepository
    {

        //
        public async Task<List<Valute>> GetData()
        {

            var todayTime = DateTime.Now.Date;
            var fileCreationTime = FileUtilities.getFileCreationTime().Date;
            var isExitsAndToday = File.Exists(FileUtilities.getPath()) && todayTime == fileCreationTime;
            if (!isExitsAndToday)
            {
                IConfiguration config = new ConfigurationBuilder()

                .AddJsonFile("appsettings.json")
                .AddEnvironmentVariables()
                .Build();


                Configs positionOptions = config.GetRequiredSection(Configs.Name).Get<Configs>();

                // Write the values to the console.
                Console.WriteLine($"KeyOne = {positionOptions.baseurl}");

                var data = DateTime.Now.ToString("dd.MM.yyyy");
                var url = string.Format(positionOptions.baseurl, data);
                var client = new HttpClient();

                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
                var content = client.GetStreamAsync(url).Result;

                XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
                var valsCurs = (ValCurs)serializer.Deserialize(content);

                WriteToXmlFile<ValCurs>(FileUtilities.getPath(), valsCurs, false);
                Console.WriteLine("The file not  exists.");
                return valsCurs.Valute;
            } else
            {
                Console.WriteLine("The file exists.");
                var list = ReadFromXmlFile<ValCurs>(FileUtilities.getPath());
                return list.Valute;
            }
        }

        T ReadFromXmlFile<T>(string filePath)
        {
            TextReader reader = null;
            try
            {
                var serializer = new XmlSerializer(typeof(T));
                reader = new StreamReader(filePath);
                return (T)serializer.Deserialize(reader);
            }
            finally
            {
                if (reader != null)
                    reader.Close();
            }
        }

        void WriteToXmlFile<T>(string filePath, T objectToWrite, bool append)
        {
            throw new NotImplementedException();
        }
    }
}
