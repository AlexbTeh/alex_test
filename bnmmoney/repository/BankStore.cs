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
        private readonly FileStore fileStore;

        public BankStore(IHttpClientService httpClientService, FileStore fileStore,  IConfigurationStore config)
        {
            this.config = config;
            this.fileStore = fileStore;
            this.httpClientService = httpClientService;
        }

        public async Task<List<Valute>> GetDataByDate(String dateTime)
        {
            var date = Convert.ToDateTime(dateTime);

            Configs positionOptions = config.getConfiguration().GetRequiredSection(Configs.Name).Get<Configs>();
            var url = string.Format(positionOptions.baseurl, date.ToString("dd.MM.yyyy"));
            var content = httpClientService.getHttpClient().GetStreamAsync(url).Result;

            XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
            var valsCurs = (ValCurs)serializer.Deserialize(content);

            fileStore.WriteToXmlFile<ValCurs>(FileUtilities.getPath(), valsCurs, false);
            Console.WriteLine("The file not  exists.");
            return valsCurs.Valute;
        }
    }
}
