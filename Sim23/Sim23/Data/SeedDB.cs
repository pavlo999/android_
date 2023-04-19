using Microsoft.EntityFrameworkCore;

namespace Sim23.Data
{
    public static class SeedDB
    {

            public static void SeedData(this IApplicationBuilder app)
            {
                using (var scope = app.ApplicationServices
                    .GetRequiredService<IServiceScopeFactory>().CreateScope())
                {
                    var context = scope.ServiceProvider.GetRequiredService<AppEFContext>();
                    context.Database.Migrate();
                }
            }
        
    }
}
