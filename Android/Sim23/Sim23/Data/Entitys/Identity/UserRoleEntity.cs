using Microsoft.AspNetCore.Identity;
using System.ComponentModel.DataAnnotations.Schema;

namespace Sim23.Data.Entitys.Identity
{
    public class UserRoleEntity:IdentityUserRole<int>
    {
   
        public UserEntity User { get; set; }
        public RoleEntity Role { get; set; }
    }
}
