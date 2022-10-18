using bnmmoney.repository;

//https://stackoverflow.com/questions/16352879/write-list-of-objects-to-a-file

namespace bnmmoney
{
    internal class Program
    {
        static void Main(string[] args)
        {

            var respository = new BankRepository();
            var valutes = respository.GetData();
            foreach (var item in valutes.Result)
            {
                Console.WriteLine( item.Name + " " + item.Value + " " + item.CharCode);
            }

        }
    }

}