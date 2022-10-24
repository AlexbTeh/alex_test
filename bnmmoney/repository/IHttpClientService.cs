using bnmmoney.module;

namespace bnmmoney.repository
{
    public interface IHttpClientService
    {
        HttpClient getHttpClient();

        Task<ValCurs> GetValCurs(string? requestUri);
    }
}
