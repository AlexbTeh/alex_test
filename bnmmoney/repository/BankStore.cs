using bnmmoney.module;

namespace bnmmoney.repository
{
    public class BankStore : IBankStore
    {
        private readonly IConfigurationStore config;
        private readonly IHttpClientService httpClientService;

        public BankStore(IHttpClientService httpClientService, IConfigurationStore config)
        {
            this.config = config;
            this.httpClientService = httpClientService;
        }

        public async Task<List<Valute>> GetDataByDate(DateTime dateTime)
        {
            var content = await httpClientService.GetValCurs(config.BaseUrl(dateTime));

            Console.WriteLine("The file not  exists.");
            if (content == null)
            {
                return new List<Valute>();
            }
            else
            {
                return content.Valute;
            }
        }
    }
}