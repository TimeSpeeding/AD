using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;
using Group13SSIS.Utility;

namespace Group13SSIS.Controllers
{
    public class AdminController : Controller
    {
        public ActionResult UserList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var userlist = db.Users.ToList();
                var uservmlist = new List<UserVM>();
                foreach (var user in userlist)
                {
                    uservmlist.Add(new UserVM()
                    {
                        UserId = user.UserId,
                        Username = user.Username,
                        Password = user.Password,
                        Name = user.Name,
                        Email = user.Email,
                        DeptId = db.Depts.Where(x => x.DeptId == user.DeptId).FirstOrDefault().Name,
                        RoleId = db.Roles.Where(x => x.RoleId == user.RoleId).FirstOrDefault().Name
                    });
                }
                ViewData["userlist"] = uservmlist;
            }
            return View();
        }

        [HttpGet]
        public ActionResult Register()
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.ToList();
                ViewData["roles"] = dc.Roles.ToList();
            }
            return View();
        }
        [HttpPost]
        public ActionResult Register(UserVM userVM)
        {
            using (Group13SSISEntities dc = new Group13SSISEntities())
            {
                ViewData["depts"] = dc.Depts.ToList();
                ViewData["roles"] = dc.Roles.ToList();
            }
            if (ModelState.IsValid)
            {
                if (UsernameVerification.IsUsernameExist(userVM.Username))
                {
                    ModelState.AddModelError("Username", "Username already exist");
                    return View(userVM);
                }
                User user = new User()
                {
                    Username = userVM.Username,
                    Password = Crypto.Hash(userVM.Password),
                    Name = userVM.Name,
                    Email = userVM.Email,
                    DeptId = Int32.Parse(userVM.DeptId),
                    RoleId = Int32.Parse(userVM.RoleId)
                };
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Users.Add(user);
                    db.SaveChanges();
                    return RedirectToAction("UserList");
                }
            }
            return View();
        }

        [HttpGet]
        public ActionResult Edit(int id)
        {
            var userVM = new UserVM();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                ViewData["depts"] = db.Depts.ToList();
                ViewData["roles"] = db.Roles.ToList();
                var user = db.Users.Where(x => x.UserId == id).FirstOrDefault();
                userVM.Username = user.Username;
                userVM.Name = user.Name;
                userVM.Email = user.Email;
                ViewBag.DeptId = user.DeptId;
                ViewBag.RoleId = user.RoleId;
            }
            return View(userVM);
        }
        [HttpPost]
        public ActionResult Edit(int id, UserVM userVM)
        {
            return View();
        }
    }
}