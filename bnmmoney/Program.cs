using bnmmoney.repository;

//https://stackoverflow.com/questions/16352879/write-list-of-objects-to-a-file

namespace bnmmoney
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var fileStore = new FileStore();
            var bankStore = new BankStore(
                new HttpClientSource(),
            new ConfigurationStore());


            var date = Convert.ToDateTime(args[0]);

            var bankWriter = new BankWriter(bankStore);
            var bankLocal = new BankLocal(fileStore, bankWriter);

            foreach (var item in bankLocal.getValutes(date).Result)
            {
                Console.WriteLine(item.Name + " " + item.Value + " " + item.CharCode);
            }

            Console.Read();

        }

    }

}