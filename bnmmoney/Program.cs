using bnmmoney.repository;

//https://stackoverflow.com/questions/16352879/write-list-of-objects-to-a-file

namespace bnmmoney
{
    internal class Program
    {
        static void Main(string[] args)
        {
            var bankStore = new BankStore(
                new HttpClientSource(),
                new FileStore(), 
                new ConfigurationStore());


     
            var valutes = bankStore.GetDataByDate(args[0]);
            foreach (var item in valutes.Result)
            {
                Console.WriteLine( item.Name + " " + item.Value + " " + item.CharCode);
            }

            Console.Read();

        }
    }

}