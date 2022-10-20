using bnmmoney.module;
using bnmmoney.utilities;
using Microsoft.Extensions.Configuration;
using System.Xml.Serialization;

namespace bnmmoney.repository
{
    public class BankStore : IBankStore
    {
        private readonly IConfigurationStore config;
        private readonly IFileStore fileStore;
        private readonly IHttpClientService httpClientService;

        public BankStore(IHttpClientService httpClientService, IFileStore fileStore, IConfigurationStore config)
        {
            this.config = config;
            this.fileStore = fileStore;
            this.httpClientService = httpClientService;
        }
        public async Task<List<Valute>> GetData()
        {

            var todayTime = DateTime.Now.Date;
            var fileCreationTime = FileUtilities.getFileCreationTime().Date;

            var isExitsAndToday =   todayTime == fileCreationTime;
            if (!isExitsAndToday)
            {
                Configs positionOptions = config.getConfiguration().GetRequiredSection(Configs.Name).Get<Configs>();
                var data = DateTime.Now.ToString("dd.MM.yyyy");
                var url = string.Format(positionOptions.baseurl, data);
                var content = httpClientService.getHttpClient().GetStreamAsync(url).Result;

                XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
                var valsCurs = (ValCurs)serializer.Deserialize(content);

                fileStore.WriteToXmlFile<ValCurs>(FileUtilities.getPath(), valsCurs, false);
                Console.WriteLine("The file not  exists.");
                return valsCurs.Valute;
            }
            else
            {
                Console.WriteLine("The file exists.");
                var list = fileStore.ReadFromXmlFile<ValCurs>(FileUtilities.getPath());
                return list.Valute;
            }
        }

        public async Task<List<Valute>> GetDataByDate(String dateTime)
        {
            var date = Convert.ToDateTime(dateTime);
            var fileCreationTime = FileUtilities.getFileCreationTime().Date;

            Console.WriteLine("date" + date + "CreationTime" + fileCreationTime);
            var isExitsAndToday =  date.Date == fileCreationTime;
            if (!isExitsAndToday)
            {
                Configs positionOptions = config.getConfiguration().GetRequiredSection(Configs.Name).Get<Configs>();
                var url = string.Format(positionOptions.baseurl, date.ToString("dd.MM.yyyy"));
                var content = httpClientService.getHttpClient().GetStreamAsync(url).Result;

                XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
                var valsCurs = (ValCurs)serializer.Deserialize(content);

                fileStore.WriteToXmlFile<ValCurs>(FileUtilities.getPath(), valsCurs, false);
                Console.WriteLine("The file not  exists.");
                return valsCurs.Valute;
            }
            else
            {
                Console.WriteLine("The file exists.");
                var list = fileStore.ReadFromXmlFile<ValCurs>(FileUtilities.getPath());
                return list.Valute;
            }
        }
    }
}
