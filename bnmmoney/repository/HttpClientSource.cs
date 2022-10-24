using bnmmoney.module;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Reflection.Metadata;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Serialization;

namespace bnmmoney.repository
{
    public class HttpClientSource : IHttpClientService
    {
        public HttpClient getHttpClient()
        {
            var client = new HttpClient();

            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            return client;
        }

        public async Task<ValCurs> GetValCurs(string? requestUri)
        {
            var content = await getHttpClient().GetStreamAsync(requestUri);
            XmlSerializer serializer = new XmlSerializer(typeof(ValCurs));
            var valsCurs = (ValCurs)serializer.Deserialize(content);
            return valsCurs;
        }
    }
}
