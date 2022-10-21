using bnmmoney.module;
using bnmmoney.utilities;
using Microsoft.Extensions.Configuration;
using System.Xml.Serialization;

namespace bnmmoney.repository
{
    public class BankStore : IBankStore
    {
        private readonly IConfigurationStore config;
        private readonly IHttpClientService httpClientService;

        public BankStore(IHttpClientService httpClientService,  IConfigurationStore config)
        {
            this.config = config;
            this.httpClientService = httpClientService;
        }

        public async Task<List<Valute>> GetDataByDate(DateTime dateTime)
        {

            Configs positionOptions = config.getConfiguration().GetRequiredSection(Configs.Name).Get<Configs>();
            var url = string.Format(positionOptions.baseurl, dateTime.ToString("dd.MM.yyyy"));
            var content = httpClientService.getHttpClient().GetStreamAsync(url).Result;

            XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
            var valsCurs = (ValCurs)serializer.Deserialize(content);

            Console.WriteLine("The file not  exists.");
            return valsCurs.Valute;
        }
    }
}
