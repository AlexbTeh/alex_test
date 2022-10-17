
using Microsoft.Extensions.Configuration;

using System.Net.Http.Headers;
using System.Reflection;
using System.Xml.Serialization;

namespace bnmmoney
{
    public class BankRepository : IBankRepository
    {

        //
        public async Task<List<Valute>> GetData()
        {
            IConfiguration config = new ConfigurationBuilder()
  
                .AddJsonFile("appsettings.json")
                .AddEnvironmentVariables()
                .Build();


            Configs positionOptions = config.GetRequiredSection(Configs.Name).Get<Configs>();

            // Write the values to the console.
            Console.WriteLine($"KeyOne = {positionOptions.baseurl}");

            var data = DateTime.Now.ToString("dd.MM.yyyy"); 
            var url = $"{positionOptions.baseurl}{data}";
            var client = new HttpClient();

            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            var content = client.GetStreamAsync(url).Result;

            XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
            var valsCurs = (ValCurs)serializer.Deserialize(content);

            return valsCurs.Valute;
        }
    }
}
