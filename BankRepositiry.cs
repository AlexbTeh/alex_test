using System.Net.Http.Headers;
using System.Xml.Serialization;

namespace bnmmoney
{
    public class BankRepository : IBankRepository
    {
        //
        public async Task<List<Valute>> getData()
        {
            var data = DateTime.Now.ToString("dd.MM.yyyy"); ;
            var url = $"https://bnm.md/ro/official_exchange_rates?get_xml=1&date={data}";
            var client = new HttpClient();

            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            var content = client.GetStreamAsync(url).Result;

            XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
            var valsCurs = (ValCurs)serializer.Deserialize(content);

            Console.WriteLine("Count massive" + valsCurs.Valute.Count);

            return valsCurs.Valute;
        }
    }
}
