using bnmmoney.module;
using bnmmoney.repository;
using bnmmoney.utilities;
using System;

//https://stackoverflow.com/questions/16352879/write-list-of-objects-to-a-file

namespace bnmmoney
{
    internal class Program
    {
        static void Main(string[] args)
        {

            var valutes = new List<Valute>();

            var fileStore = new FileStore();
            var bankStore = new BankStore(
                new HttpClientSource(),
                fileStore,
            new ConfigurationStore());

            var date = Convert.ToDateTime(args[0]);
            var fileCreationTime = FileUtilities.getFileCreationTime().Date;

            var bankWriter = new BankWriter(bankStore, args[0]);
            var bankLocal = new BankLocal(fileStore);

            var isExitsAndToday = date.Date == fileCreationTime;
            if (!isExitsAndToday)
            {
                valutes = bankWriter.getValutes().Result;
            }else
            {
                valutes = bankLocal.getValutes().Result;
            }

            foreach (var item in valutes)
            {
                Console.WriteLine( item.Name + " " + item.Value + " " + item.CharCode);
            }

            Console.Read();

        }

    }

}