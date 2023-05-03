using Microsoft.EntityFrameworkCore;
using Sim23.Data.Entitys;

namespace Sim23.Data
{
    public class AppEFContext : DbContext
    {
        public AppEFContext(DbContextOptions<AppEFContext> options)
            : base(options)
        { }

        public DbSet<CategoryEntity> Categories { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            // flyent validation...
        }
    }
}
