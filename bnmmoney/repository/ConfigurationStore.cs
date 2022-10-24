using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    public class ConfigurationStore : IConfigurationStore
    {
       public IConfiguration getConfiguration()
        {
            return new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .AddEnvironmentVariables()
            .Build();
        }

        public IConfigurationSection GetRequiredSection(string name)
        {
            return getConfiguration().GetRequiredSection(name);
        }

        public string BaseUrl(DateTime dateTime)
        {
            string url = GetRequiredSection(Configs.Name).Get<Configs>().baseurl;
            return string.Format(url, dateTime.ToString("dd.MM.yyyy"));
        }
    }
}
