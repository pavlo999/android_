using Sim23.Data.Entitys.Identity;

namespace Sim23.Abstract
{
    public interface IJwtTokenService
    {
        Task<string> CreateToken(UserEntity user);
    }
}
