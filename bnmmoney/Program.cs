using bnmmoney.repository;

//https://stackoverflow.com/questions/16352879/write-list-of-objects-to-a-file

namespace bnmmoney
{
    public class Program
    {
        static void Main(string[] args)
        {
            getData(args[0]);

        }

        public static void getData(string time)
        {
            var fileStore = new FileStore();
            var bankStore = new BankStore(
                new HttpClientSource(),
            new ConfigurationStore());


            var date = DateTime.Parse(time);

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