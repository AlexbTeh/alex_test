using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    internal class ConfigurationStore : IConfigurationStore
    {
        IConfiguration IConfigurationStore.getConfiguration()
        {
            return new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .AddEnvironmentVariables()
            .Build();
        }
    }
}
