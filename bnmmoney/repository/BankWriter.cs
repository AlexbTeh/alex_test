using bnmmoney.module;

namespace bnmmoney.repository
{
    public class BankWriter : IBankDecorator
    {
        private readonly BankStore bankStore;
        public BankWriter(BankStore bankStore)
        {
            this.bankStore = bankStore;
        }
       public  Task<List<Valute>> getValutes(DateTime dateTime)
        {
            return bankStore.GetDataByDate(dateTime);
        }
    }
}
