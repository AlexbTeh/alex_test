using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    public class HttpClientSource : IHttpClientService
    {
        HttpClient IHttpClientService.getHttpClient()
        {
            var client = new HttpClient();

            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            return client;
        }
    }
}
