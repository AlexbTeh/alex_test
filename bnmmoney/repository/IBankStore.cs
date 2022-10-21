using bnmmoney.module;

namespace bnmmoney.repository
{
    public interface IBankStore
    {
        Task<List<Valute>> GetDataByDate(DateTime dateTime);
    }
}
