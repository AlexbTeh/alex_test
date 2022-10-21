using bnmmoney.module;
using bnmmoney.utilities;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace bnmmoney.repository
{
    public class BankWriter : IBankDecorator
    {
        private readonly BankStore bankStore;
        private readonly string dateTime;
        public BankWriter(BankStore bankStore , String dateTime)
        {
            this.bankStore = bankStore;
            this.dateTime = dateTime;
        }
       public  Task<List<Valute>> getValutes()
        {
            return bankStore.GetDataByDate(dateTime);
        }
    }
}
